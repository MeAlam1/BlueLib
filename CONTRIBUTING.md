# Contributing to BlueLib

## Coding Conventions

<strong>Please Read Before Contributing</strong>

### Naming Conventions
- We use **camelCase** for all variable and method names. This means no spaces or underscores, and each new word after the first begins with a capital letter.
    - **Example**: `thisIsAVariable`, `thisIsAMethod()`.

- All method parameters should start with a lowercase `p` to indicate that they are parameters.
    - **Example**: `String getParameter(String pVariantName, String pParameterKey)`.

### Commenting

- All code should be commented using the `/** * * */` format. This ensures that comments are displayed when users hover over a method, class, or variable, even after the library is compiled. This helps maintain clarity and consistency in the documentation.

#### Commenting steps:

#### 1. **Purpose**:
- **What the code or section of code does.**
- Clearly describe **why** this block of code exists and its primary function.

#### 2. **When**:
- Specify **when** this code is executed or what triggers it.

#### 3. **Where**:
- Indicate **where** this code resides or is invoked.

#### 4. **Additional Information**:
- Include any **important details** that will help someone understand the code's context or behavior.
- Mention **dependencies**, assumptions, or any other relevant notes.

#### 5. **Versioning**:
- Include the `@since` tag to specify the version in which this code was introduced or modified.

#### 6. **Author**:
- Include the `@author` tag to specify who created the code.

#### 7. **See**:
- Use the `@see` tag to link to other classes, methods, or resources that are relevant to the code.


#### Example Format:
```java
    /**
 * Applies the Markdown feature to the provided component.
 * <p>
 * Purpose: This method checks whether the feature is enabled and applies the corresponding formatting to the given {@link MutableComponent}. If the feature is disabled, it logs an informational message and returns the original component.<br>
 * When: The method is called when the Markdown feature needs to be applied to a component.<br>
 * Where: It is called in the {@link MarkdownParser#parseMarkdown(Component)} method.<br>
 * Additional Info: The method uses a {@link Pattern} to find text matching the specified prefix and suffix for Markdown formatting.<br>
 * </p>
 *
 * @param pComponent The component to apply Markdown formatting to.
 * @return The component with the applied Markdown formatting.
 * @author MeAlam
 * @see MarkdownParser#parseMarkdown(Component)
 * @see MutableComponent
 * @see Pattern
 * @since 1.6.0
 */
public MutableComponent apply(MutableComponent pComponent) {
    // Method implementation
}
```

#### 8. **Classes**:
- For classes, include a Key Methods section that lists out the main methods provided by the class.
- Include the `@version` tag to indicate the version of the class.
- **Example Format**:
```java
/**
 * A class responsible for parsing and formatting Markdown into Minecraft's {@link Component}.
 * <p>
 * This class processes text components and applies Markdown-style formatting ({@link Bold}, {@link Italic}, {@link Strikethrough}, {@link Underline}, {@link Spoiler}, {@link Hyperlink}, {@link Color}, {@link CopyToClipboard})
 * to the text. The formatting is controlled globally or individually through the {@link EnableMarkdownFor} and
 * {@link DisableMarkdownFor} inner classes.
 * </p>
 * <p>
 * Purpose: This class provides a utility for parsing and applying Markdown formatting in Minecraft chat messages.<br>
 * When: This class is used when a message needs to be formatted with Markdown syntax.<br>
 * Where: The class is invoked in various components where text formatting is required, typically in chat rendering or message construction.<br>
 * Additional Info: The formatting can be enabled or disabled globally or selectively for specific features like bold or italic. The settings are managed via the {@link EnableMarkdownFor} and {@link DisableMarkdownFor} classes.
 * </p>
 * Key Methods:
 * <ul>
 * <li>{@link #parseMarkdown(Component)} - Parses and applies Markdown formatting to a given message component.</li>
 * <li>{@link #enableMarkdown()} - Enables global Markdown formatting.</li>
 * <li>{@link #disableMarkdown()} - Disables global Markdown formatting.</li>
 * <li>{@link #enableMarkdownFor()} - Returns an instance of {@link EnableMarkdownFor} to enable specific Markdown features.</li>
 * <li>{@link #disableMarkdownFor()} - Returns an instance of {@link DisableMarkdownFor} to disable specific Markdown features.</li>
 * </ul>
 *
 * @author MeAlam
 * @version 1.6.0
 * @see EnableMarkdownFor
 * @see DisableMarkdownFor
 * @see Bold
 * @see Italic
 * @see Strikethrough
 * @see Underline
 * @see Spoiler
 * @see Hyperlink
 * @see Color
 * @see CopyToClipboard
 * @since 1.1.0
 */
public class MarkdownParser {
    // Class implementation
}
```

- **General Guidelines**:
    - Always ensure that comments are clear, concise, and provide enough information to understand the code.
    - When referencing variables or constants, use `{@code}` to wrap them within the comment.
    - Use `{@link}` to refer to classes, methods, or any other Java elements where appropriate.
    - Key Methods: In class-level comments, list out key methods provided by the class, which can help users quickly understand the main functionalities. 
    - Versioning: Include the `@since` tag in both class-level and method-level comments to indicate the version since which the class or method has been available.
    - Update: If you update a Class, please add/update the `@version` to indicate it has been changed.
    - Copyright: Each file should start with `// Copyright (c) BlueLib. Licensed under the MIT License.`
    - Tags: Use `@see` to link to the correct Wiki Documentation page if it exists.
    - Logging: Log steps using `BaseLogger.log`. Remember to add true as the last parameter to ensure it's a BlueLib log.
    - Error Handling: Always ensure that errors and warnings are logged using appropriate logging levels. Critical steps must be logged at least with `BaseLogger.log(BaseLogLevel.Error)` to keep a trail of execution.

### Deprecation

- If you optimize a method, variable, or class and determine that it is no longer necessary for the library, mark it as `@Deprecated` instead of removing it. This only applies to elements that have been included in previous released versions of the library.
- Include a **strong TODO comment** explaining why it is deprecated and any further action required, such as testing or eventual removal.
- Include an **`@see`** that links to the New Method
- **Example**:
  ```java
  /**
   * Builds and returns a map of the parameters added to this builder. <br><br>
   * Building custom parameters will not go through this method anymore. However, it has not been tested with Multiple Entities and Datapack yet. <br>
   * <strong>TODO: Testing with Multiple Entities and Datapacks required before Deletion/Refactoring.</strong><br>
   * @return A map containing the parameters added to this builder.
   * @author MeAlam
   * @since 1.0.0
   * @see #newMethod()
   */
  @Deprecated
  public Map<String, String> build() {
      return new HashMap<>(parameters);
  }
  ```

## Workflow

> **Note:** The examples provided in this Workflow assume the use of Git commands. However, you can also use GitHub's web interface or the tools provided by your IDE, which are equally recommended.

1. **Fork the Repository**
    - Create a fork of the BlueLib repository to your GitHub account.

2. **Clone Your Fork**
    - Clone your forked repository to your local machine.
    - **Example**:
      ```bash
      git clone https://github.com/MeAlam1/BlueLib.git
      ```

3. **Create a Branch**
    - Create a new branch for your changes. The branch name should be descriptive of the work being done.
    - **Example**:
      ```bash
      git checkout -b feature/improve-logging
      ```

4. **Import the Project**
    - Import the project into your Integrated Development Environment (IDE) and allow it to build.
    - **Example IDEs**:
        - IntelliJ IDEA (Recommended)
        - Visual Studio Code (VSC)
        - Eclipse (Recommended)

5. **Modify the Library**
    - Make the necessary changes to the respective library folder you are working on. Ensure you adhere to the coding conventions described above.

6. **Test Your Changes**
    - Before committing, test your changes by running the game using the appropriate test mod loader folder.
      - Use the `test` package to test your changes.
      - If no code is available to test, create new test code in the `test` package.
      - Since modifying the existing test code is not recommended, adding Javadocs to comment on the test code is excessive and unnecessary.
    - Ensure that your changes do not introduce any issues or regressions.

7. **Run Gradle tasks**
    - To ensure everything is clean, run the Gradle spotless tasks
    - **Required**:
      ```bash
        gradlew spotlessApply
      ```
    - **Optional**:
      ```bash
        gradlew spotlessCheck
      ```

8. **Commit Your Changes**
    - Once you are satisfied with your changes, commit them to your branch.
    - Write clear and concise commit messages explaining the changes made.
    - **Example**:
      ```bash
      git commit -am "Improved logging functionality and deprecated old log method"
      ```

9. **Push to Your Fork**
    - Push your branch to your fork on GitHub.
    - **Example**:
      ```bash
      git push origin feature/improve-logging
      ```

10. **Create a Pull Request (PR)**
   - Navigate to your fork on GitHub and create a Pull Request to the main repository. Provide a detailed description of the changes made and why they are necessary.

## Contributor License Agreement (CLA)

### License

By contributing to this project, you agree that your contributions are licensed under the MIT License, as included in this repository. This means that your contributions can be freely used, modified, and distributed under the same terms as the rest of the project.

### Copyright Assignment

You assign all rights, title, and interest in the copyright of your contributions to BlueLib. This allows BlueLib to enforce copyright and maintain the integrity of the project.

## Developer's Certificate of Origin (DCO)

By submitting your contribution, you certify the following:

- The contribution is your original work, or you have the right to submit it.
- You grant BlueLib the right to use, modify, and distribute your contribution under the MIT License.
- You agree to assign the copyright of your contribution to BlueLib.

If you have any questions or need further clarification, please reach out to the project maintainers before submitting your contribution.

---

Thank you for contributing to BlueLib and helping to make it better!

---
