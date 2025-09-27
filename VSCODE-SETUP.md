# API Automation Framework - VS Code Setup Guide

Welcome to your migrated API Automation Framework! This guide will help you transition smoothly from Eclipse to VS Code.

## 🚀 Quick Start

### Essential VS Code Extensions (Already Installed)

- ✅ **Extension Pack for Java** - Complete Java development support
- ✅ **Maven for Java** - Maven integration
- ✅ **Test Runner for Java** - Run and debug tests
- ✅ **REST Client** - Test APIs directly in VS Code
- ✅ **XML** - XML file support for Maven
- ✅ **vscode-icons** - Better file icons

## 🔧 Key Features Set Up

### 1. **Run & Debug Tests**

- **Run single test**: Right-click on test method → "Run Test"
- **Debug test**: Right-click on test method → "Debug Test"
- **Run all tests**: Use Command Palette (Cmd+Shift+P) → "Java: Run Tests"
- **Use Debug Panel**: Pre-configured launch configs for LoginAPITest and UserDetailsAPITest

### 2. **Maven Integration**

Access Maven tasks via:

- **Terminal Menu**: Terminal → Run Task → Choose Maven task
- **Command Palette**: Cmd+Shift+P → "Tasks: Run Task"
- **Available tasks**:
  - Maven: Clean
  - Maven: Compile
  - Maven: Test
  - Maven: Package
  - Maven: Install
  - Run TestNG Suite

### 3. **REST Client for API Testing**

- Open `api-tests.http` file
- Click "Send Request" above any HTTP request
- Test your APIs without external tools
- Switch between dev/qa environments easily

### 4. **Project Structure Navigation**

- **Explorer Panel**: Shows project structure (better than Eclipse Project Explorer)
- **Java Projects View**: Shows Java-specific project structure
- **Test View**: Dedicated panel for running and viewing tests

## 🎯 Eclipse vs VS Code - Key Differences

### Running Tests

| Eclipse                            | VS Code                   |
| ---------------------------------- | ------------------------- |
| Right-click → Run As → TestNG Test | Right-click → Run Test    |
| Run Configuration dialog           | Launch.json configuration |
| Console view                       | Terminal/Debug Console    |

### Maven Operations

| Eclipse                             | VS Code                                 |
| ----------------------------------- | --------------------------------------- |
| Right-click project → Maven → Goals | Terminal → Run Task → Maven commands    |
| Maven view in IDE                   | Integrated terminal with Maven commands |
| m2e plugin                          | Native Maven support                    |

### Code Navigation

| Eclipse                      | VS Code                     |
| ---------------------------- | --------------------------- |
| Ctrl+Shift+T (Open Type)     | Cmd+T (Go to Symbol)        |
| Ctrl+Shift+R (Open Resource) | Cmd+P (Quick Open)          |
| F3 (Go to Declaration)       | F12 (Go to Definition)      |
| Ctrl+H (Search)              | Cmd+Shift+F (Find in Files) |

### Debugging

| Eclipse                    | VS Code                       |
| -------------------------- | ----------------------------- |
| Debug perspective          | Debug view (side panel)       |
| Variables/Expressions view | Variables panel in Debug view |
| Breakpoints view           | Breakpoints panel             |

## 🛠️ Productivity Tips

### 1. **Keyboard Shortcuts (macOS)**

- `Cmd+Shift+P` - Command Palette (most important!)
- `Cmd+P` - Quick file open
- `Cmd+T` - Go to symbol
- `Cmd+Shift+F` - Find in files
- `Cmd+D` - Select next occurrence
- `Cmd+/` - Toggle comment
- `Shift+Alt+F` - Format document

### 2. **IntelliSense & Auto-completion**

- Type and get suggestions automatically
- `Ctrl+Space` - Trigger suggestions manually
- Auto-import on save (configured)
- Organize imports automatically

### 3. **Integrated Terminal**

- `Ctrl+\`` - Toggle terminal
- Run Maven commands directly: `mvn test`, `mvn clean compile`
- Multiple terminal instances supported

### 4. **Source Control (Git)**

- Built-in Git support (better than Eclipse EGit)
- Source Control panel shows changes
- Commit, push, pull directly from IDE
- Built-in merge conflict resolution

## 📁 Project Configuration Files

### `.vscode/launch.json`

- Debug configurations for your tests
- Pre-configured for LoginAPITest and UserDetailsAPITest
- Supports debugging with breakpoints

### `.vscode/tasks.json`

- Maven build tasks
- Run via Terminal → Run Task
- Integrated with VS Code's task system

### `.vscode/settings.json`

- Java development optimizations
- Auto-save, formatting, and organization
- TestNG as default test runner
- File exclusions for cleaner workspace

## 🧪 Testing Workflow

### Running Individual Tests

1. Open test file (e.g., `LoginAPITest.java`)
2. Click "Run Test" above test method
3. View results in Test Results panel

### Running All Tests

1. Press `Cmd+Shift+P`
2. Type "Java: Run Tests"
3. Or use Terminal → Run Task → "Maven: Test"

### Debugging Tests

1. Set breakpoints by clicking line numbers
2. Right-click test method → "Debug Test"
3. Use Debug panel to step through code
4. Inspect variables in Variables panel

## 📊 Test Reports

- TestNG reports: `target/surefire-reports/index.html`
- Open in browser for detailed results
- VS Code Test Results panel for quick overview

## 🔍 Common Issues & Solutions

### Problem: Tests not appearing in Test Explorer

**Solution**: Reload Java projects with `Cmd+Shift+P` → "Java: Reload Projects"

### Problem: Maven not working

**Solution**: Check `mvn --version` in terminal, ensure Maven is installed

### Problem: Java version issues

**Solution**: Check `java --version`, configure in settings if needed

### Problem: IntelliSense not working

**Solution**: `Cmd+Shift+P` → "Java: Restart Language Server"

## 🎉 You're Ready!

Your API Automation Framework is now fully configured for VS Code! The setup includes:

✅ All necessary extensions installed  
✅ Debug configurations ready  
✅ Maven tasks configured  
✅ REST client for API testing  
✅ Optimized settings for Java development  
✅ Git integration working

**Next Steps:**

1. Try running a test: Right-click on a test method → "Run Test"
2. Test the REST client: Open `api-tests.http` and send a request
3. Explore the Command Palette: `Cmd+Shift+P` and start typing
4. Set up your actual API endpoints in the configuration files

Happy coding! 🚀
