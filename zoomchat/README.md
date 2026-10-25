# Zoom & Chat Preview (Fabric, Minecraft 26.3)

- Hold C: zoom (scroll wheel while held adjusts zoom level)
- Hold Z: show chat history without opening chat
- Both are rebindable in Options > Controls.

Setup
1. Install JDK 25.
2. Go to https://fabricmc.net/develop, pick 26.3, and copy the Loader, Fabric API and Loom versions
   into gradle.properties and build.gradle (the REPLACE_ME spots).
3. Run `gradle wrapper` once (or copy the wrapper from the Fabric example mod), then `./gradlew build`.
4. The jar is in build/libs/. Use `./gradlew runClient` to test.

No local setup? Push this folder to a GitHub repo. The included workflow (.github/workflows/build.yml)
builds the jar for you: Actions tab > latest run > Artifacts > zoomchat-jar.
The workflow fills in the REPLACE_ME versions automatically, so you don't need to edit anything.
