package com.ray.common.encrypt;

public final class Base64 {
    private static final int BASELENGTH = 128;
    private static final int LOOKUPLENGTH = 64;
    private static final int TWENTYFOURBITGROUP = 24;
    private static final int EIGHTBIT = 8;
    private static final int SIXTEENBIT = 16;
    private static final int FOURBYTE = 4;
    private static final int SIGN = -128;
    private static final char PAD = '=';
    private static final boolean fDebug = false;
    private static final byte[] base64Alphabet = new byte[128];
    private static final char[] lookUpBase64Alphabet = new char[64];

    public Base64() {
    }

    private static boolean isWhiteSpace(char aChar) {
        return aChar == 32 || aChar == 13 || aChar == 10 || aChar == 9;
    }

    private static boolean isPad(char aChar) {
        return aChar == 61;
    }

    private static boolean isData(char aChar) {
        return aChar < 128 && base64Alphabet[aChar] != -1;
    }

    public static String encode(byte[] bytes) {
        if(bytes == null) {
            return null;
        } else {
            int L1 = bytes.length * 8;
            if(L1 == 0) {
                return "";
            } else {
                int var2 = L1 % 24;
                int var3 = L1 / 24;
                int var4 = var2 != 0?var3 + 1:var3;
                char[] var17 = new char[var4 * 4];

                int var11 = 0;
                int var12 = 0;

                byte var14;
                byte var18;
                byte var19;
                byte var20;
                byte var21;
                for(int var13 = 0; var13 < var3; ++var13) {
                    var20 = bytes[var12++];
                    var21 = bytes[var12++];
                    byte var22 = bytes[var12++];
                    var19 = (byte)(var21 & 15);
                    var18 = (byte)(var20 & 3);
                    var14 = (var20 & -128) == 0?(byte)(var20 >> 2):(byte)(var20 >> 2 ^ 192);
                    byte var15 = (var21 & -128) == 0?(byte)(var21 >> 4):(byte)(var21 >> 4 ^ 240);
                    byte var16 = (var22 & -128) == 0?(byte)(var22 >> 6):(byte)(var22 >> 6 ^ 252);
                    var17[var11++] = lookUpBase64Alphabet[var14];
                    var17[var11++] = lookUpBase64Alphabet[var15 | var18 << 4];
                    var17[var11++] = lookUpBase64Alphabet[var19 << 2 | var16];
                    var17[var11++] = lookUpBase64Alphabet[var22 & 63];
                }

                byte var23;
                if(var2 == 8) {
                    var20 = bytes[var12];
                    var18 = (byte)(var20 & 3);
                    var23 = (var20 & -128) == 0?(byte)(var20 >> 2):(byte)(var20 >> 2 ^ 192);
                    var17[var11++] = lookUpBase64Alphabet[var23];
                    var17[var11++] = lookUpBase64Alphabet[var18 << 4];
                    var17[var11++] = 61;
                    var17[var11++] = 61;
                } else if(var2 == 16) {
                    var20 = bytes[var12];
                    var21 = bytes[var12 + 1];
                    var19 = (byte)(var21 & 15);
                    var18 = (byte)(var20 & 3);
                    var23 = (var20 & -128) == 0?(byte)(var20 >> 2):(byte)(var20 >> 2 ^ 192);
                    var14 = (var21 & -128) == 0?(byte)(var21 >> 4):(byte)(var21 >> 4 ^ 240);
                    var17[var11++] = lookUpBase64Alphabet[var23];
                    var17[var11++] = lookUpBase64Alphabet[var14 | var18 << 4];
                    var17[var11++] = lookUpBase64Alphabet[var19 << 2];
                    var17[var11++] = 61;
                }

                return new String(var17);
            }
        }
    }

    public static byte[] decode(String text) {
        if(text == null) {
            return null;
        } else {
            char[] var1 = text.toCharArray();
            int var2 = removeWhiteSpace(var1);
            if(var2 % 4 != 0) {
                return null;
            } else {
                int var3 = var2 / 4;
                if(var3 == 0) {
                    return new byte[0];
                } else {
                    int var13 = 0;
                    int var14 = 0;
                    int var15 = 0;

                    byte[] var17;
                    byte var18;
                    byte var19;
                    byte var20;
                    byte var21;
                    char var22;
                    char var23;
                    char var24;
                    char var25;
                    for(var17 = new byte[var3 * 3]; var13 < var3 - 1; ++var13) {
                        if(!isData(var22 = var1[var15++]) || !isData(var23 = var1[var15++]) || !isData(var24 = var1[var15++]) || !isData(var25 = var1[var15++])) {
                            return null;
                        }

                        var18 = base64Alphabet[var22];
                        var19 = base64Alphabet[var23];
                        var20 = base64Alphabet[var24];
                        var21 = base64Alphabet[var25];
                        var17[var14++] = (byte)(var18 << 2 | var19 >> 4);
                        var17[var14++] = (byte)((var19 & 15) << 4 | var20 >> 2 & 15);
                        var17[var14++] = (byte)(var20 << 6 | var21);
                    }

                    if(isData(var22 = var1[var15++]) && isData(var23 = var1[var15++])) {
                        var18 = base64Alphabet[var22];
                        var19 = base64Alphabet[var23];
                        var24 = var1[var15++];
                        var25 = var1[var15++];
                        if(isData(var24) && isData(var25)) {
                            var20 = base64Alphabet[var24];
                            var21 = base64Alphabet[var25];
                            var17[var14++] = (byte)(var18 << 2 | var19 >> 4);
                            var17[var14++] = (byte)((var19 & 15) << 4 | var20 >> 2 & 15);
                            var17[var14++] = (byte)(var20 << 6 | var21);
                            return var17;
                        } else {
                            byte[] var16;
                            if(isPad(var24) && isPad(var25)) {
                                if((var19 & 15) != 0) {
                                    return null;
                                } else {
                                    var16 = new byte[var13 * 3 + 1];
                                    System.arraycopy(var17, 0, var16, 0, var13 * 3);
                                    var16[var14] = (byte)(var18 << 2 | var19 >> 4);
                                    return var16;
                                }
                            } else if(!isPad(var24) && isPad(var25)) {
                                var20 = base64Alphabet[var24];
                                if((var20 & 3) != 0) {
                                    return null;
                                } else {
                                    var16 = new byte[var13 * 3 + 2];
                                    System.arraycopy(var17, 0, var16, 0, var13 * 3);
                                    var16[var14++] = (byte)(var18 << 2 | var19 >> 4);
                                    var16[var14] = (byte)((var19 & 15) << 4 | var20 >> 2 & 15);
                                    return var16;
                                }
                            } else {
                                return null;
                            }
                        }
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    private static int removeWhiteSpace(char[] var0) {
        if(var0 == null) {
            return 0;
        } else {
            int var1 = 0;
            int var2 = var0.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                if(!isWhiteSpace(var0[var3])) {
                    var0[var1++] = var0[var3];
                }
            }

            return var1;
        }
    }

    static {
        int var0;
        for(var0 = 0; var0 < 128; ++var0) {
            base64Alphabet[var0] = -1;
        }

        for(var0 = 90; var0 >= 65; --var0) {
            base64Alphabet[var0] = (byte)(var0 - 65);
        }

        for(var0 = 122; var0 >= 97; --var0) {
            base64Alphabet[var0] = (byte)(var0 - 97 + 26);
        }

        for(var0 = 57; var0 >= 48; --var0) {
            base64Alphabet[var0] = (byte)(var0 - 48 + 52);
        }

        base64Alphabet[43] = 62;
        base64Alphabet[47] = 63;

        for(var0 = 0; var0 <= 25; ++var0) {
            lookUpBase64Alphabet[var0] = (char)(65 + var0);
        }

        var0 = 26;

        int var1;
        for(var1 = 0; var0 <= 51; ++var1) {
            lookUpBase64Alphabet[var0] = (char)(97 + var1);
            ++var0;
        }

        var0 = 52;

        for(var1 = 0; var0 <= 61; ++var1) {
            lookUpBase64Alphabet[var0] = (char)(48 + var1);
            ++var0;
        }

        lookUpBase64Alphabet[62] = 43;
        lookUpBase64Alphabet[63] = 47;
    }

}
