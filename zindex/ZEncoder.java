package zindex;

import global.Descriptor;

public class ZEncoder {

    public static String encode(Descriptor desc) {
        int[] point = new int[5];
        for (int i = 0; i < 5; i++) {
            point[i] = desc.get(i);
        }
        return encodeArray(point);
    }

    public static String encodeArray(int[] desc) {
        // Construct the binary vector of the point
        String[] binVector = new String[5];
        StringBuilder builder;
        for (int i = 0; i < 5; i++) {
            builder = new StringBuilder(16);
            String bitStr = Integer.toBinaryString(desc[i]);
            // Fill with 0s
            if (bitStr.length() < 16) {
                for (int j = 0; j < 16 - bitStr.length(); j++) {
                    builder.append('0');
                }
            }
            builder.append(bitStr);
            binVector[i] = builder.toString();
        }

        // Interleaving
        StringBuilder result = new StringBuilder(80);
        for (int i = 0; i < 16; i++) {
            result.append(binVector[4].charAt(i));
            result.append(binVector[3].charAt(i));
            result.append(binVector[2].charAt(i));
            result.append(binVector[1].charAt(i));
            result.append(binVector[0].charAt(i));
        }

        return result.toString();
    }

    public static Descriptor decodeAsDesc(String zvalue) {
        Descriptor desc = new Descriptor();
        desc.set(decodeAsArray(zvalue));
        return desc;
    }

    public static int[] decodeAsArray(String zvalue) {
        StringBuilder[] binVectors = new StringBuilder[5];
        for (int i = 0; i < 5; i++) {
            binVectors[i] = new StringBuilder(16);
        }
        // Reverse the z-value
        for (int i = 0; i < 80; ) {
            for (int j = 0; j < 5; j++) {
                binVectors[j].append(zvalue.charAt(i));
                i++;
            }
        }

        int[] result = new int[5];
        for (int i = 4; i >= 0; i--) {
            result[i] = Integer.parseInt(binVectors[4-i].toString(), 2);
        }
        return result;
    }
}