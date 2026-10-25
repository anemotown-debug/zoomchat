"""Looks up the right Fabric versions for the Minecraft version in gradle.properties
and writes them into gradle.properties / build.gradle (replacing REPLACE_ME)."""
import json, re, sys, urllib.request
import xml.etree.ElementTree as ET

def get(url):
    with urllib.request.urlopen(url, timeout=30) as r:
        return r.read().decode()

def maven_versions(url):
    root = ET.fromstring(get(url))
    return [v.text for v in root.iter("version")], root.findtext("versioning/latest")

props = open("gradle.properties").read()
mc = re.search(r"^minecraft_version=(.+)$", props, re.M).group(1).strip()

loaders = json.loads(get("https://meta.fabricmc.net/v2/versions/loader"))
loader = next(l["version"] for l in loaders if l.get("stable"))

api_versions, _ = maven_versions("https://maven.fabricmc.net/net/fabricmc/fabric-api/fabric-api/maven-metadata.xml")
matching = [v for v in api_versions if v.endswith("+" + mc)]
if not matching:
    sys.exit(f"No Fabric API found for Minecraft {mc} yet. Try an older version in gradle.properties.")
api = matching[-1]

_, loom = maven_versions("https://maven.fabricmc.net/net/fabricmc/fabric-loom/net.fabricmc.fabric-loom.gradle.plugin/maven-metadata.xml")

print("Minecraft:", mc, "| Loader:", loader, "| Fabric API:", api, "| Loom:", loom)

props = props.replace("loader_version=REPLACE_ME", f"loader_version={loader}")
props = props.replace("fabric_api_version=REPLACE_ME", f"fabric_api_version={api}")
open("gradle.properties", "w").write(props)

b = open("build.gradle").read().replace("version 'REPLACE_ME'", f"version '{loom}'")
open("build.gradle", "w").write(b)
