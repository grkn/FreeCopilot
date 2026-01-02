# FreeCopilot for individual users

It is a intellij plugin that helps individual developers to use explaining, refactoring, answering questions.

* You need to have intellij to build it and run it.
* Java version is 21
* It is designed for developers
* Don't forget that you need to use your "own api key" for requests.

## How to use it

- If you are using Windows, you need to install ubuntu.

  wsl --install 

  wsl.exe --install Ubuntu (this comments download and runs Ubuntu inside Windows)
  
- You need run freecopilot project in intellij by "runIde" option in gradle.
- Then you can create simply project and use it.

  <img width="1523" height="1038" alt="image" src="https://github.com/user-attachments/assets/d0a97239-28b9-4c00-865a-db4b956dbfae" />




Also don't forget to create you api key from openai platform and use it.

'''
public String getApiKey() {
        // Modify here to test yourself. Add api key from open ai platform and paste it here
        return "your-api-key-for-openai";
}
'''

### There are three options for now. Explain, Refactor, Ask Questions about Coding for now.

It can be changed or reusable because, you can create any intellij plugin.

You need to know javax.swing, java, some basic concepts for plugin development.



