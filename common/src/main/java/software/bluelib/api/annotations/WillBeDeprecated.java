/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.annotations;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated element is planned to be deprecated in a future version,
 * possibly followed by removal at a later date.
 * <p>
 * Unlike {@link Deprecated}, which communicates that an element is already discouraged for use,
 * this annotation signals an <b>early notice</b> that the element may be deprecated in the future.
 * The element is still fully supported and no active deprecation or removal work has begun.
 * Use this to communicate upcoming API changes and allow users to prepare.
 * </p>
 *
 * <p>
 * This annotation is purely informational unless tooling or build rules are configured
 * to treat it as a warning.
 * </p>
 *
 * <ul>
 * <li> The {@code since} attribute can be used to specify when the removal plan was announced. </li>
 * <li> The {@code reason} attribute can provide additional context for the planned removal. </li>
 * <li> The {@code plannedRemovalVersion} attribute can specify the target version for actual removal. </li>
 * <li> The {@code alternatives} attribute can list recommended alternatives to the annotated element. </li>
 * <li> The {@code showWarning} attribute indicates if tools should emit a warning when this annotation is present. </li>
 * </ul>
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(value = { CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, MODULE, PARAMETER, TYPE })
public @interface WillBeDeprecated {

	/**
	 * Returns the version in which the removal plan for the annotated element was announced.
	 * The version string should match the format and namespace of the {@code @since} javadoc tag.
	 * Default is the empty string.
	 *
	 * @return the version string
	 */
	String since() default "";

	/**
	 * Provides the reason or context for the planned removal of the annotated element.
	 * Default is the empty string.
	 *
	 * @return the reason for planned removal
	 */
	String reason() default "";

	/**
	 * Specifies the target version in which the annotated element is planned to be actually removed.
	 * This is for informational purposes and may help users plan migrations.
	 * Default is the empty string.
	 *
	 * @return the planned removal version
	 */
	String plannedRemovalVersion() default "";

	/**
	 * <p>
	 * The date (ISO-8601, e.g., "2025-08-06") when the removal plan was announced.
	 * </p>
	 * This is useful for tracking the timeline of deprecation and removal plans.
	 * It can help users understand how long they have to adapt their code before the element is
	 * actually removed.
	 *
	 * @return Default is the empty string.
	 */
	String announcedDate() default "";

	/**
	 * Lists recommended alternatives to the annotated element.
	 * This can help users migrate away from the element before it is removed.
	 * Default is an empty array.
	 *
	 * @return an array of alternative element names or descriptions
	 */
	String[] alternatives() default {};

	/**
	 * Indicates if tools should emit a warning when this annotation is present.
	 * This can be used by static analysis tools or IDEs to notify users.
	 * Default is {@code false}.
	 *
	 * @return {@code false} if a warning should be shown, {@code true} otherwise
	 */
	boolean showWarning() default false;
}
