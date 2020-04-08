package com.click.force.clickforce.working.time.GifView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class ClickforceImage {

    public static Bitmap openimage(){
        String encodedMuteImage = "iVBORw0KGgoAAAANSUhEUgAABEwAAAD9CAYAAACm2OJ5AAAAAXNSR0IArs4c6QAAAARzQklUCAgI\n" +
                "    CHwIZIgAACAASURBVHic7N1rbFzpeSf4/3tudc6pIqtYVSSrWGSJFCk1RUkttqwQglfJOJmduOEs\n" +
                "    HOfLBNPYmfHuYmEMdrAINkAwCwP5EATYL4sAwQ42O85ixgYSAzuYBTKNjdEG4iS2Zcdmd6vZrRsl\n" +
                "    8VpFsu6n7lXn+r77gaRsT+KkxeJN0vNrEAKk5jnve1gQcP56n+dhIK+13/yN/+aGJKQvA+zL9Xot\n" +
                "    VrcsVCuVF7pGOpPBRCaDkXh8SwB/xiTx9X/37//k45NZ8evhRz/4wa8D4ksAvvy9v/pLfO+v/hKP\n" +
                "    Hz480rVmZmfxj3/18/jHn38bkXDkzxhjf3bl2rVvHO+KXy6O4/w6gC8JIb5cLhZRLhVRKZVg1S1U\n" +
                "    y2VYtRoajToa9To67TZ63R46nQ5s24bj2PBcF0EQgHMOIcTz6yqKAkVRoGoaDMOAYZqIRCKIDA0j\n" +
                "    FoshNjKCWDyOkXgc4+MpJEfHkJ6YAICvQ5K+PjMz892zeianaWYsdoMx5csAvswYix3lGleuXsWV\n" +
                "    q9eQmZzaAsOfSZL4+v/82//mXP2983/94R/+umDiSwD78oNPPsbDTz5BPrc98HXHU2mMp9OYymYh\n" +
                "    gK+D4+v/xx//8Wvx2SGEEEIIOU3SWS+AEEIIIYQQQggh5LxRznoBhBByUorFYrjVamUZC+asai2+\n" +
                "    /vQpnj19hv/7j/7o5nBseM7QDTQbDTQbdTQbDXS7XbTbbXQ7bfR6PfS6XTi2Dcdx4dg2PN+D73kI\n" +
                "    ggBCiJ85XQIAnHMEQQC4LgAgCAJ4nod+30a300HdqsMslRAOh7EdjWJoaAjR2Aiajfpcfnv77d/4\n" +
                "    1V+dnpqewvTsZUzPzmJmehrTs7PQFSXHVDWn6/r6WTxH8ul98v3vj9iSNCcYm/tgefl2aW9vzrIs\n" +
                "    7O3soNvtHPm6kiTtn2BSVYynU5i7dAlXr78JMEDiAP74j49vE4QQQgghBAAFJoSQV1iv14sAwQI4\n" +
                "    e1sIzEEwMADrT58k+3Y/aff76Pd76Pf6zwMRz/Xg+x58338ejgQBf16CwwWH+C9KcQ4JIZ7/f0EQ\n" +
                "    wHVd9Pt9yHLroFxHhaIqUBUVqqZBVVWEQiF4njfn+34kFArdFoKBCQF2eH0hwIG7wvfvAqDA5Jxz\n" +
                "    JSnOGVtiEG8zJqZrtWry8cOH6LTb6Ha7R76uJEnQQiEYhoFUOo25y5dx8xd+4eAP//ZnkRBCCCGE\n" +
                "    DI4CE0LIK0EIYbTbbTMIgsi9e/fw0Ucf4S+//e3JyampNw3T+Gy71Vqo1+votDsolYqolMso7O0+\n" +
                "    D0Y458exhudBShAEgOd9qu8LhyOpcCSSisfj6Pd6aLWbqFs1RCJh6IYOxiTXtnuNf/tv//fVKzNX\n" +
                "    MHPlCmZmZnwAPQAdxtinuxE5EUIIuVqtmgAiW2trl9vN+k3f83+p3WoNNxt15HPb+5+NAT5jqqYh\n" +
                "    GoshnkgglZ7opTOTvYsXZzsAqpKi2Me2GUIIIYQQ8hwFJoSQV0K1Ws0GQTDPGFuE70MOAqw+ehBb\n" +
                "    f/Z0XlGUuOPYsGo1WLUams0GOu32/gmSn3Na5DR5not+r4s69oOWbreDUrGIzfU1xBNJKIqSBXBH\n" +
                "    UuQIlwDGBBzHaQBYDYVCKwBKZ7qB11y9Xo8wxuaFEItciMX81vbC7u6OtrW5gUq5/HeWb31ajDEw\n" +
                "    xjA0NISZixdxef4KJjKZnGHoq4yxFQixwhgrHvOWCCGEEEIIKDAhhLwqfD/NgJsAviiw/3La7/a0\n" +
                "    YrMQbTSsaK/bg+M4B1NuvP1+JL4/0Mvs8S3dBxcCnuej1++hXreg7+7CDJswzTASo8l0IpE0J7PZ\n" +
                "    aSGAg+3tAVAdx9kEBSZnKggCE8AshPhlQCw2GvX444cP1XKxiGazMdDJEsYYJEmCGQ4jMzmFq2++\n" +
                "    iejwcMEIh+8Jxt6VZNnSdd06vt0QQgghhJBDFJgQQl5af/Uf/oPe0vV4wFj8wYMHi7KqLiqytLib\n" +
                "    z6NaKaPRqKNaqaBY2EOnc/SGmyeNcw7OOXzPg2330QbADpt8Kgoyncmo67pRLRTKhsMRaKEQ+r3+\n" +
                "    mBCiDiC3srICw/Cty5c/YzHGBq8tIp+KEGIEQLxSqVy2qtVF13UWy6XSG+VSCfncNpqNBryDBsBH\n" +
                "    IcsyTNNEOBLB6Nh4P5FMWpmJSSs8FF5hkFYikci5GqNMCCGEEPKqocCEEPLSaul63JPEkuBYevLk\n" +
                "    8bV2szXb7XZQLpZQLhVRq1bR7XbgDvDSemaEAA8C+EKgblkIggCddhu1SgX53DaSo2ORaDS6kM5k\n" +
                "    EB9NZh1HWt7a2loGQP0sTs8c53yJB8HNSqU0m9/ajm9tbmAnn0ev04Hnuvu9bI5IC4WQHBvDZPYC\n" +
                "    Uqm0FQrpy4Hwl5lgD2QIagBMCCGEEHLCKDAhhLy0AsbigmOJQbxTr9WSa0+fqo8fPTwYBezAc93n\n" +
                "    pzdeNocTd4IgQN2y0Gw2sbezg51cDkPDUaQzE5H5hYWFkURiDkJkmUDH9/0VUGByaoIgmAPwNoBf\n" +
                "    6rTb6rPVx9rq6mMU9vbQ7Xbhfcqmvz+PpmlIjo7h8hvzSKXTlqzIy+1q/ZvZ6dlqem+PGv0SQggh\n" +
                "    hJwwCkwIIS8FIcQUfD9bazaTjz75BB99+CE+vPfBjCrJ15jEkrs7O+FioYBmvf48aHgZg5K/y/OS\n" +
                "    HQDtdhu+74MxSJoWCsmyEtrdzU8qknIrZOit//inf7qmSVLhS//sn+XPet2vuvX1dY0JEbFq1eGd\n" +
                "    XB47uzsol0rotNsDnSzRDQOGYSAWG+mFQnpBUdVcKKSvCMYe2IVCdWJioneM2yCEEEIIIT8HBSaE\n" +
                "    kJeD72c5cAecz4MJCCbQabXirVZzttftqY16Ha1m43kD1bNu5HpSfM+DDaBRr4MxBrvfRyKZjI6O\n" +
                "    j12fnr5oKCHpR0EQ3ANAgckJ23r2DAJAo1HH7k4elVIZzUYDtm0P9PkzDAPxRAKx2EhPU9V1z3Hv\n" +
                "    MkmsyJDW9wA6WUIIIYQQckooMCGEvBQCxuYgxNtCiM8BDEwAjmOjsLuLrY2NgcsfXha+78P3ffT7\n" +
                "    fVi1GjbW15GeyMRv3rq1lM1OLwkwU0iiBeAHZ73WV93qw4cQTKDVbGFrYwOFvV3UrcEH1oTDEYyO\n" +
                "    jSORTHaEEI+KpZ13v/p7v0cNXgkhhBBCThkFJoSQc0sIMeX7flYIMbf2dPVOo95I1SoVbG6s49mT\n" +
                "    Vezt7KLVbL4ypTcvSgCAEOh1O8jntvfHz5rhrK6H7vyff/iHYBJfUwK29j/+1m/R2OFj8t3vfneK\n" +
                "    sSArCTb3w+9//05hby9Vq1ZRKZdg20dvHyPL8v5UJFXFRCaD+YUFTF3IAgKQJQH8u39/jLsghBBC\n" +
                "    CCGfBgUmhJBzy/f9LMDvAOLter2RWn34MLW5vo7C3i5y21uoVauw+/3XNjDBQelRt9tFfnsbdcvC\n" +
                "    eCqdTU9kzMTo6JwQ7L1AkjoAKDA5JowFWSbYHSHwdhDwVHFvL7W9tQXb7sMZIDBRFAUhXYduGEhP\n" +
                "    ZnDl6lVcnr8CMECSX83yMkIIIYSQ844CE0LIuSKEUAFEAESsanXB5/4twcXnKuUSNtbX8Oj+fdSt\n" +
                "    GirlMnq9k+99yRjb/5IkSD/1Kw5+/6cWDrG//v2RwAeNWg/DnJPsqeI6DmqOg1q1CgBJwzCSzUZz\n" +
                "    XpalqqqGtr7xjW9UFUXpdTqdzle+8pXXo3bpGAkh1GazGQmCbmTl3v2Ffs++Ffj+5zqdNmq1KkrF\n" +
                "    wsD30A0D0VgM8UQC6dREZ3JyqjMzO7cjAZYCvIRzsQkhhBBCXn4UmBBCzpu47/uLkiQtVquVxXKp\n" +
                "    NFevW3h0/z62NzcPgpIuPN8/lcXIigJVVaFp2sFXCKqmQpZkyIoMxhiEEOBcgPOD6TxBANu2Yds2\n" +
                "    XMdBwDkC3z+VRrTtVgu7O3l4nodobGQuk5l8O8F5Co6";
        byte[] decodedString = Base64.decode(encodedMuteImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    private void imageToBase64(){
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.clickforce_logo);
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//        byte[] bytes = byteArrayOutputStream.toByteArray();
//        byte[] encode = Base64.encode(bytes, Base64.DEFAULT);
//
//        String ss = new String(encode);
    }

}
