package mirror.android.os;

import android.os.IBinder;

import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;

/**
 * @author Lody
 */

public class Bundle {
    public static Class<?> TYPE = RefClass.load(Bundle.class, android.os.Bundle.class);

    @MethodParams({String.class, IBinder.class})
    public static RefMethod<Void> putIBinder;

    @MethodParams({String.class})
    public static RefMethod<IBinder> getIBinder;
}
