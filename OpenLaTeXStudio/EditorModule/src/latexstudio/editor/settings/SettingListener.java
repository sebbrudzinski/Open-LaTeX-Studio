/*                    
 * Copyright (c) 2016 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation declares a method is supposed to handle changes in the setting passed in its constructor.
 * The method is supposed to match the Setting its registered as a listener for.
 * 
 * That is, the type of its single parameter should match <code>setting</code>'s <code>SettingType</code>:
 * <ul>
 *  <li><code>SettingType.INT</code>     -> int</li>
 *  <li><code>SettingType.STRING</code>  -> String</li>
 *  <li><code>SettingType.BOOLEAN</code> -> boolean</li>
 * <ul>
 * 
 * @author Lord_Farin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SettingListener {

    /**
     * Determines which <code>Setting</code> this Annotation pertains to.
     * @return
     */
    ApplicationSettings.Setting setting();
}