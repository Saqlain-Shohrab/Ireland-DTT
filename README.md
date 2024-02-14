# Project README

This project is a demo quiz project. Given below are the setup instructions for configuring external files, creating JSON data, adding PNG files to the `drawable` directory, and implementing Firebase. It will help to perfectly run the app without any issues.

## Setup Instructions

Follow these steps to set up your project:

### Step 1: Create `externalConfig.gradle` File

1. Create an `externalConfig.gradle` file in the root directory of your project.
2. Add the following content to the `externalConfig.gradle` file:

    ```groovy
    ext {
        debugSigningConfig = [
            keyAlias: 'aliasname',
            keyPassword: 'password',
            storeFile: file('path to debug key'),
            storePassword: 'password'
        ]
    }

    ext {
        releaseSigningConfig = [
            keyAlias: 'alias name',
            keyPassword: 'password',
            storeFile: file('path to release key'),
            storePassword: 'password'
        ]
    }
    ```

   Replace `'aliasname'`, `'password'`, `'path to debug key'`, and `'path to release key'` with your actual values.

### Step 2: Add `output_final_img_.json` File

1. Create an `output_final_img_.json` file.
2. Place the `output_final_img_.json` file in the `/app/src/main/res/raw` directory.
3. Structure your JSON data as follows:

    ```json
    {
      "questions": [
        {
          "position": 1,
          "Question": "Question 1?",
          "Options": [
            "Option1",
            "Option2",
            "Option3",
            "Option4"
          ],
          "type": "Type1/Type2",
          "explanation": "Explanation",
          "image": ""
        },
        {
          "position": 2,
          "Question": "Question 2?",
          "Options": [
            "Option1",
            "Option2",
            "Option3",
            "Option4"
          ],
          "type": "Type1/Type2",
          "explanation": "Explanation",
          "image": ""
        }
      ]
    }
    ```

   Replace the placeholders with your actual question data. Be sure to change the 'QuestionType' enum class for matching with type field of the json. 

### Step 3: Add PNG Files to `drawable` Directory

1. Create PNG files corresponding to each question in the `/app/src/main/res/drawable` directory.
2. Name each PNG file with the prefix `image_` followed by the `position` field value from the JSON structure. For example, if the position field value is `1`, name the PNG file `image_1.png`.

## Firebase Implementation

Implement Firebase in your project by following these steps:

1. Navigate to the [Firebase Console](https://console.firebase.google.com/).
2. Create a new Firebase project or select an existing one.
3. Follow the documentation provided at [Firebase Cloud Messaging Documentation](https://firebase.google.com/docs/cloud-messaging) to integrate Firebase Cloud Messaging into your Android app.

Make sure to follow the Firebase setup instructions carefully to ensure proper integration with your project.
