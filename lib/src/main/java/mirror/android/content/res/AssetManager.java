package mirror.android.content.res;

import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefMethod;

/**
 * @author Lody
 */

public class AssetManager {
    public static Class<?> TYPE = RefClass.load(AssetManager.class, android.content.res.AssetManager.class);
    public static RefConstructor<android.content.res.AssetManager> ctor;
    @MethodParams(String.class)
    public static RefMethod<Integer> addAssetPath;
}
