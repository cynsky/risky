package au.gov.amsa.ais;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import au.gov.amsa.util.nmea.NmeaMessage;
import au.gov.amsa.util.nmea.NmeaUtil;
import au.gov.amsa.util.nmea.Talker;

public class AisNmeaMessageTest {

	@Test
	public void testAisNmeaMessage() {
		String m = "$BSVDM,2,2,5,B,00000000008,2*33";
		AisNmeaMessage a = new AisNmeaMessage(m);
		assertEquals("B", a.getChannel());
		assertEquals("$BSVDM", a.getFormat());
		assertEquals(2, a.getFragmentCount());
		assertEquals(2, a.getFragmentNumber());
		assertEquals("5", a.getSequentialMessageId());
		assertEquals("33", a.getChecksum());
		assertEquals(Talker.UNKNOWN, a.getTalker());
		assertNotNull(a.getNmea());
		a.getMessage();

	}

	@Test(expected = AisParseException.class)
	public void testInvalidLine() {
		new AisNmeaMessage("");
	}

	@Test(expected = AisParseException.class)
	public void testInvalidLineLessThan7Columns() {
		new AisNmeaMessage("$BSVDM,2,2*4E");
	}

	@Test(expected = AisParseException.class)
	public void testHasNoChecksum() {
		new AisNmeaMessage("$BSVDM,2,2,5,B,00000000008,2");
	}

	@Test(expected = AisParseException.class)
	public void testChecksumDoesNotMatch() {
		new AisNmeaMessage("$BSVDM,2,2,5,B,00000000008,2*34");
	}

	@Test(expected = AisParseException.class)
	public void testRuntimeExceptionBecomesAisParseException() {
		new AisNmeaMessage(
				"$GPGGA,024654.00,3351.0141,S,15117.2167,E,1,06,1.06,00105,M,021,M,,*77");
	}

	@Test
	public void testGetTimeObtainsTimeFromTagBlock() {
		String line = "\\g:1-2-1234,s:r3669961,c:1120959341*51\\$BSVDM,2,2,5,B,00000000008,2*33";
		AisNmeaMessage m = new AisNmeaMessage(line);
		assertEquals(1120959341000L, (long) m.getTime());
	}

	@Test
	public void testGetTimeObtainsTimeFromTagBlock2() {
		String line = "\\c:1357563697*00\\!AIVDM,1,1,,B,13b2AH8000bkvNajJ=1ov2C>25`4,0*0F";
		AisNmeaMessage m = new AisNmeaMessage(line);
		assertEquals(1357563697000L, (long) m.getTime());
		assertEquals(1357563697000L, m.getTimestampedMessage().time());
	}

	@Test
	public void testFragmentCount() {
		String line = "\\g:1-2-1536,c:1334258609*2F\\!BSVDM,2,1,0,A,577V7s02?k61I8Lg<00Dq@E918U<F1=@58000016Op`BL5D8tIm5@PDPCp0T,0*5B";
		NmeaMessage m = NmeaUtil.parseNmea(line);
		List<String> items = m.getItems();
		assertEquals("!BSVDM", items.get(0));
		assertEquals("2", items.get(1));
		assertEquals("1", items.get(2));
	}

}
