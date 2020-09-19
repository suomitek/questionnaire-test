package cn.csl.basics.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Guid {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
	
	public static long GetHashCode(UUID uuid) {
		long low  = uuid.getLeastSignificantBits();
		long high = uuid.getMostSignificantBits();
		
        long val = uuid.toString().hashCode() & 0xffffffffl;
        val |= high << 24 & 0x7f00000000000000l;
        
        val |= high << 48 & 0xff000000000000l;
        val |= low << 8 & 0xff0000000000l;
        val |= low << 32 & 0xff00000000l;

        return val;
    }
	
	public static void main(String[] args) {
		UUID uuid = UUID.randomUUID();
		System.out.println(uuid.toString());
		System.out.println(uuid.toString().hashCode());
		long high = uuid.toString().hashCode();
		System.out.println(high);
		high = high << 32;
		System.out.println(high);
	}
	
	public static String newUUIDString() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static long getGuid() {
		UUID guid = UUID.randomUUID();
		long low = guid.toString().hashCode();
		long high = guid.hashCode();
		high = high << 32;
		return high | low;
	}
	
	public static String getGuidString() {
		UUID guid = UUID.randomUUID();
		long low = guid.toString().hashCode() & 0x7fffffff;
		long high = guid.hashCode() & 0x7fffffff;
		high = high << 32;
		return String.valueOf(high | low);
	}
	
	public static String getUUIDHashString() {
		return String.valueOf(UUID.randomUUID().hashCode() & 0x7fffffff);
	}
	
	public static String getTimeString() {
		UUID guid = UUID.randomUUID();
		long low = guid.hashCode() & 0x7fffffff;
		long high = Integer.parseInt(sdf.format(new Date()));
		high = high << 31;
		return String.valueOf(high | low);
	}
	
	public static long getTimeGuid() {
		UUID guid = UUID.randomUUID();
		long low = guid.hashCode() & 0x7fffffff;
		long high = Integer.parseInt(sdf.format(new Date()));
		high = high << 31;
		return high | low;
	}
	
	public static String get64String() {
		return UUID.randomUUID().toString().replaceAll("-", "") +
				UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	private UUID gguid = UUID.randomUUID();
	private long longGuid;
	
	public Guid() {
		long low = gguid.hashCode() & 0x7fffffff;
		long high = Integer.parseInt(sdf.format(new Date()));
		high = high << 31;
		longGuid = high | low;
	}
	
	public String guidString() {
		return gguid.toString().replaceAll("-", "");
	}
	
	public String timeString() {
		return String.valueOf(longGuid);
	}
	
	public long guidId() {
		return longGuid;
	}
	
	public static long getPosGuid(String posId, Date date) {
		long low = date.getTime() / 1000;
		long high = posId.hashCode() & 0x7fffffff;
		high = high << 32;
		return high | low;
	}
}
