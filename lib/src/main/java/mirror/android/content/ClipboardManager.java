package mirror.android.content;

import android.os.IInterface;

import mirror.RefClass;
import mirror.RefStaticMethod;
import mirror.RefStaticObject;

public class ClipboardManager {
    public static Class<?> TYPE = RefClass.load(ClipboardManager.class, android.content.ClipboardManager.class);
    public static RefStaticMethod<IInterface> getService;
    public static RefStaticObject<IInterface> sService;
}
