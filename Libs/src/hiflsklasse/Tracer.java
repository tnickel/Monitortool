package hiflsklasse;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tracer
{
	// 0 for no tracing,
	// 10 for error messages only;
	// 20 for error messages plus warning messages;
	// 30 for error, warning, and some additional information;
	// 40 for the most detailed tracing which includes debugging messages and
	// all previously mentioned messages.

	// the trace file prefix string, which determins where
	// the file will be created and how it will be named
	static String m_strTraceFilePrefix = "Trace";
	// the trace level, which determines how much info will be
	// written to the trace file at run-time
	static int m_nTraceLevel = 20;
	// the output stream for the trace file
	static BufferedOutputStream m_traceStream = null;
	// the date-time when the trace file is created
	static Date m_dateTraceStart = new Date();

	// set the trace file prefix, which will close the current trace file
	static public final void SetTraceFilePrefix(String strFilePrefix)
	{
		if (strFilePrefix != null)
		{
			// the operation is thread-safe
			synchronized (m_strTraceFilePrefix)
			{
				// assign the new value
				m_strTraceFilePrefix = strFilePrefix;
				// close the current trace file so that a new one
				// will be created at a later time
				try
				{
					if (m_traceStream != null)
						m_traceStream.close();
				} catch (Exception e)
				{
				}
				m_traceStream = null;
			}
		}
	}

	// set the current trace level
	static public final void SetTraceLevel(int nLevel)
	{
		// the operation is thread-safe
		synchronized (m_strTraceFilePrefix)
		{
			// the trace level is non-negative
			m_nTraceLevel = nLevel > 0 ? nLevel : 0;
		}
	}

	// write a trace message to the current trace file
	static public final void WriteTrace(int nLevel, String strMsg)
	{
		// return immediately if the current trace level is too small
		if (nLevel > m_nTraceLevel)
			return;
		// the operation is thread-safe

		System.out.println("Tracer:" + strMsg);

		synchronized (m_strTraceFilePrefix)
		{
			try
			{
				// first, get the new date-time stamp
				Date traceDate = new Date();
				// if it is a new day already, then close the current trace file
				// so that a new one can be created
				if (traceDate.getTime() / 86400000 != m_dateTraceStart
						.getTime() / 86400000)
				{
					if (m_traceStream != null)
						m_traceStream.close();
					m_traceStream = null;
				}
				// create a new trace file if not already created
				if (m_traceStream == null)
				{
					// construct the new for the new trace file
					SimpleDateFormat formatter = new SimpleDateFormat(
							"_yyyyMMdd_HHmmssSSS");
					// String strFileName =
					// m_strTraceFilePrefix+formatter.format(traceDate)+".txt";
					String strFileName = m_strTraceFilePrefix;
					// create the new trace file with constructed name
					// append trace (append = true-flag)
					m_traceStream = new BufferedOutputStream(
							new FileOutputStream(strFileName, true));
					// save the date-time stamp
					m_dateTraceStart = traceDate;
				}
				// write the trace message to the current trace file
				if (m_traceStream != null)
				{
					// prefix the trace message with date-time stamp and the
					// thread name
					// append the trace message with a new line
					SimpleDateFormat formatter = new SimpleDateFormat(
							"HH:mm:ss_SSS");
					m_traceStream
							.write((formatter.format(traceDate) + "("
									+ Thread.currentThread().getName() + "): "
									+ strMsg + "\r\n").getBytes());
					m_traceStream.flush();
				}
			} catch (Exception e)
			{
				m_traceStream = null;
			}
		}
		if (nLevel == 10)
		{
			UI ui = new UI();
			ui.FehlermeldungStop(strMsg);
		}
	}
}
