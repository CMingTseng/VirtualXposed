package mirror.android.content;

import android.content.Intent;
import android.os.Bundle;

import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;

/**
 * @author Lody
 */

public class IIntentReceiver {
    public static Class<?> TYPE = RefClass.load(IIntentReceiver.class, "android.content.IIntentReceiver");

    @MethodParams({Intent.class, int.class,
            String.class, Bundle.class, boolean.class, boolean.class})
    public static RefMethod<Void> performReceive;


}
