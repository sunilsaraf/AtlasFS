# Contributing to AtlasFS

Thank you for your interest in contributing to AtlasFS! This document provides guidelines and information for contributors.

## Code of Conduct

Please be respectful and constructive in all interactions with the community.

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6+
- Git
- Protocol Buffers compiler (for proto changes)

### Setting Up Development Environment

1. Fork the repository
2. Clone your fork:
   ```bash
   git clone https://github.com/your-username/AtlasFS.git
   cd AtlasFS
   ```

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Run tests:
   ```bash
   mvn test
   ```

## Development Workflow

### Branching Strategy

- `main` - stable release branch
- `develop` - active development branch
- `feature/*` - feature branches
- `bugfix/*` - bug fix branches
- `release/*` - release preparation branches

### Making Changes

1. Create a feature branch:
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. Make your changes following the coding standards

3. Add tests for your changes

4. Run the test suite:
   ```bash
   mvn test
   ```

5. Commit your changes with a descriptive message:
   ```bash
   git commit -m "Add feature: description of your changes"
   ```

6. Push to your fork:
   ```bash
   git push origin feature/your-feature-name
   ```

7. Create a Pull Request

## Coding Standards

### Java Style

- Follow Google Java Style Guide
- Use 4 spaces for indentation
- Maximum line length: 100 characters
- Use meaningful variable and method names

### Naming Conventions

- Classes: PascalCase
- Methods: camelCase
- Constants: UPPER_SNAKE_CASE
- Packages: lowercase

### Code Organization

- Keep classes focused and single-purpose
- Prefer composition over inheritance
- Use interfaces for abstraction
- Document public APIs with Javadoc

### Testing

- Write unit tests for all new code
- Aim for > 80% code coverage
- Use descriptive test names
- Follow AAA pattern (Arrange, Act, Assert)

Example:
```java
@Test
public void testMkdir_CreatesDirectoryWithCorrectPermissions() {
    // Arrange
    long parentInode = 1L;
    String name = "testdir";
    int mode = 0755;
    
    // Act
    MkDirResponse response = service.mkdir(parentInode, name, mode, 0, 0);
    
    // Assert
    assertEquals(mode, response.getAttributes().getMode());
}
```

## Protocol Buffers

When modifying `.proto` files:

1. Maintain backward compatibility
2. Use field numbers > 15 for new fields
3. Mark deprecated fields with `[deprecated = true]`
4. Add comments for all messages and fields
5. Regenerate code: `mvn protobuf:compile`

## Pull Request Process

### Before Submitting

- [ ] Code follows style guidelines
- [ ] Tests pass locally
- [ ] New tests added for new functionality
- [ ] Documentation updated
- [ ] Commit messages are clear and descriptive

### PR Description

Include:
- Summary of changes
- Related issue number (if applicable)
- Testing performed
- Screenshots (for UI changes)

### Review Process

1. Automated checks must pass (CI, tests, linting)
2. At least one maintainer approval required
3. Address review comments
4. Squash commits if requested
5. Maintainer will merge when ready

## Reporting Bugs

### Before Reporting

- Check existing issues
- Verify the bug in the latest version
- Collect relevant information

### Bug Report Template

```markdown
**Describe the bug**
A clear description of what the bug is.

**To Reproduce**
Steps to reproduce:
1. 
2. 
3. 

**Expected behavior**
What you expected to happen.

**Actual behavior**
What actually happened.

**Environment**
- OS: 
- Java version:
- AtlasFS version:

**Additional context**
Any other relevant information.
```

## Feature Requests

### Feature Request Template

```markdown
**Feature Description**
Clear description of the proposed feature.

**Use Case**
Why is this feature needed? What problem does it solve?

**Proposed Solution**
How should this feature work?

**Alternatives**
Other approaches considered.

**Additional Context**
Any other relevant information.
```

## Documentation

### Types of Documentation

- **Code Comments**: For complex logic
- **Javadoc**: For all public APIs
- **README**: Getting started guide
- **DESIGN.md**: Architecture and design decisions
- **Tutorials**: Step-by-step guides

### Documentation Style

- Use clear, concise language
- Include code examples
- Keep documentation up-to-date with code changes
- Use proper markdown formatting

## Communication

### Channels

- GitHub Issues: Bug reports, feature requests
- GitHub Discussions: Questions, ideas, general discussion
- Pull Requests: Code review and discussion

### Response Times

- We aim to respond to issues within 2-3 business days
- Pull requests reviewed within 1 week
- Security issues addressed with highest priority

## License

By contributing to AtlasFS, you agree that your contributions will be licensed under the same license as the project.

## Questions?

If you have questions not covered here, please:
1. Check existing documentation
2. Search closed issues
3. Open a new discussion
4. Reach out to maintainers

Thank you for contributing to AtlasFS!
