# HaveANiceDayAndroid ![Build Status](https://www.bitrise.io/app/b10220c409c51793.svg?token=Klq9TcBlbOQqX1i2MIquMQ)
Android app to generate smiles thanks to https://github.com/pedrovgs/HaveANiceDay
I knnow, it's not the prettiest APP you'll see, but we're working on it :(


# Configuration
This project uses FireBase and Crashlytics so before building it you need to add the configuration files for both of these services.

## Google Services:
1. Join to [firebase](https://console.firebase.google.com/)
2. Create the application
3. Download the `google-services.json` from https://console.firebase.google.com/
4. Place the downloaded file to `mobile/google-services.json`

## Fabric
* You can place the fabric.properties file containing 
```
apiSecret=<<your secret here>>
apiKey=<<your key here>>
```
* You can provide your fabirc's credentials at the first build
`./gradlew  assemble -DfabricSecret=<<your secret here>>-DfabricKey=<<your key here>>`

## IDE configuration

I wanted to try [Room](https://developer.android.com/topic/libraries/architecture/room.html) which is currently in beta,
you'll need to get at least Android Studio 3 which, at the moment, is also in beta.
You can get the preview [here](https://developer.android.com/studio/preview/index.html)

# Flavors & BuildTypes
This app has configured three flavors and two build types:

1. PreProduction will receive notifications from a different topic. It's for development purposes. 
2. Beta has same configuration than production. This is the version we'll upload to Fabric to test the build
3. Production

Each the flavor can be built for 

1. Debug with debug traces enabled which is cool for development. **Fabric won't allow you uplading debug versions**
2. Release with no debug traces. You can upload these type of builds to Fabric. 

# Build

You can just build the app by executing:

```./gradlew assemble```


# Run the tests

At this moment we have no automated test :(

# Uploading to Fabric

Just execute:

``./gradlew crashlyticsUploadDistributionBetaRelease`` for beta

or

``./gradlew crashlyticsUploadDistributionPreRelease`` for PRE

It will generate an automatic notification to every user in `${ENV}_distribution_emails.txt
 

# Contributing

If you would like to contribute code to this repository you can do so through GitHub by creating a new branch in the repository and sending a pull request or opening an issue.
Please, remember that there are some requirements you have to pass before accepting your contribution:

* Write clean code and test it.
* The code written will have to match the product owner requirements.
* Follow the repository code style.
* Write good commit messages.
* Do not send pull requests without checking if the project build is OK and CI status is success.
* Review if your changes affects the repository documentation and update it.
* Describe the PR content and don't hesitate to add comments to explain us why you've added or changed something.