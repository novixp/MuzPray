name: Build Debug APK

on:
  push:
    branches: [ "main", "master" ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout Code
        uses: actions/checkout@v4

      - name: Inspect File Layout
        run: |
          echo "=== ROOT DIRECTORY ==="
          ls -la
          echo "=== APP DIRECTORY ==="
          ls -la app || echo "No app directory found"

      - name: Set up Java 17
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'

      - name: Setup Gradle
        uses: gradle/actions/setup-gradle@v3
        with:
          gradle-version: '8.7'

      - name: List Available Tasks
        run: gradle tasks --all

      - name: Build Debug APK
        run: gradle :app:assembleDebug --stacktrace
