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

  - **Structure**:
      - **Class Descriptions**: 
        - **Key Methods:** List key methods provided by the class, using bullet points for easy readability.
        - **Author:** If you have contributed to a Method/Class, put yourself as Co-author, if you have created an Method/Class, put yourself as Author.
        - **Since Version:** Use the `@since` tag to indicate the version since which the class has been available.
            **Example:**
          ```java
          /**
           * A {@code JSONLoader} class responsible for loading and parsing JSON data from resources
           * defined by {@link ResourceLocation} within a Minecraft mod environment.
           * <p> 
           * Key Methods:
           * <ul>
           *   <li>{@link #loadJson(ResourceLocation, ResourceManager)} - Loads a JSON resource.</li>
           * </ul>
           * @author MeAlam
           * @Co-author Dan
           * @since 1.0.0
           */
          public class JSONLoader {
          }
          ```
      - **Method Descriptions**: Begin each comment with a brief description that typically starts with `A ...`, where the `...` represents a link to the relevant class or object using `{@link ClassName}`. If the method is `void`, use `{@code}` to refer to the method name instead.
          - **Example for a Method**:
            ```java
            /**
             * A {@link String} that retrieves the value of a custom parameter for a specific variant.
             *
             * @param pVariantName {@link String} - The variant name you want to see the custom parameter of.
             * @param pParameterKey {@link String} - The parameter you want to see.
             * @return The value of the custom parameter identified by {@code pParameterKey}
             * for the variant specified by {@code pVariantName}.
             * @author MeAlam
             * @Co-author Dan
             * @since 1.0.0
               */
              public String getCustomParameter(String pVariantName, String pParameterKey) {
                  // Method implementation
              }
              ```

      - **Parameters**: Start each parameter description with `{@link TypeOfParameter} - [Comment]`. If the parameter is referenced within the comment, enclose it in a code block using `{@code}`.
          - **Example for Parameters**:
            ```java
            /**
             * A {@link String} that retrieves the value of a custom parameter for a specific variant.
             *
             * @param pVariantName {@link String} - The variant name you want to see the custom parameter of.
             * @param pParameterKey {@link String} - The parameter you want to see.
             * @return The value of the custom parameter identified by {@code pParameterKey}
             * for the variant specified by {@code pVariantName}.
             * @author MeAlam
             * @Co-author Dan
             * @since 1.0.0
             */
             public String getCustomParameter(String pVariantName, String pParameterKey) {
                  // Method implementation
             }
             ```

- **General Guidelines**:
    - Always ensure that comments are clear, concise, and provide sufficient information to understand the code.
    - When referencing variables or constants, use `{@code}` to wrap them within the comment.
    - Use `{@link}` to refer to classes, methods, or any other Java elements where appropriate.
    - Key Methods: In class-level comments, list out key methods provided by the class, which can help users quickly understand the main functionalities. 
    - Versioning: Include the `@since` tag in both class-level and method-level comments to indicate the version since which the class or method has been available.
      - If you update a Class/Method, please add/update the `@version` to indicate it has been changed.
    - Copyright: Each file should start with `// Copyright (c) BlueLib. Licensed under the MIT License.`

### Deprecation

- If you optimize a method, variable, or class and determine that it is no longer necessary for the library, mark it as `@Deprecated` instead of removing it. This only applies to elements that have been included in previous released versions of the library.

- Include a **strong TODO comment** explaining why it is deprecated and any further action required, such as testing or eventual removal.
- **Example**:
  ```java
  /**
   * Builds and returns a map of the parameters added to this builder. <br><br>
   * Building custom parameters will not go through this method anymore. However, it has not been tested with Multiple Entities and Datapack yet. <br>
   * <strong>TODO: Testing with Multiple Entities and Datapacks required before Deletion/Refactoring.</strong><br>
   * @return A map containing the parameters added to this builder.
   * @author MeAlam
   * @Co-author Dan
   * @since 1.0.0
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

5. **Publish to Local Maven Repository**
    - Run the following command from the library folder (e.g., `NeoForge`, `Forge`, `Fabric`) to publish the library to your local Maven repository.
    - **Example**:
      ```bash
      ./gradlew publishToMavenLocal
      ```
    - This allows you to test the library locally.

6. **Modify the Library**
    - Make the necessary changes to the respective library folder you are working on. Ensure you adhere to the coding conventions described above.

7. **Test Your Changes**
    - Before committing, test your changes by running the game using the appropriate test mod loader folder:
        - `TestMods/TestNeoForge`
        - `TestMods/TestForge`
        - `TestMods/TestFabric`
    - Ensure that your changes do not introduce any issues or regressions.

8. **Commit Your Changes**
    - Once you are satisfied with your changes, commit them from the root folder (`BlueLib`).
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
