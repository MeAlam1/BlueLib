// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A custom annotation that acts as a boolean flag.
 * If this annotation is present on a class or method, it is considered 'true'.
 * Otherwise, it defaults to 'false'.
 *
 * @author MeAlam
 * @version 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface EnableLogging {
}
