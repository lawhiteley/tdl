# Test-Driven Lighting
*Ever wanted your Gradle build's test results fed to your Hue bulbs? No? Fair enough, but it means you're in the wrong place because this plugin does exactly that.*

## Usage

### 
*This plugin isn't hosted anywhere at present but can be used from a local Maven repository if checked out and built.*
```kotlin
plugins {
	id("io.whiteley.luke.tdl") version "1.0.0"
}
```

### Configuration
```kotlin
tdl {
	roomName = ""
	bridgeIp = ""
	hueApiKey = ""
}
```

`roomName` - Is case-sensitive and self-explanatory.

`bridgeIp` - If you don't have it to hand then follow the steps [here](https://huetips.com/help/how-to-find-my-bridge-ip-address/).

`hueApiKey` - Philips have a [guide](https://developers.meethue.com/develop/get-started-2/) for grabbing this.

## Disclaimer
This plugin is naught but a toy - I would strongly recommend against checking it in to any codebase you care about.

## Caveats
* Doesn't yet return lights to their previous state after signalling a result.
* Supplying the bridge's IP is currently mandatory but bridge autodiscovery is supported by YAHA and could be utilised here in the future.

## Notes
All the heavy lifting in this plugin is being done by the [yetanotherhueapi](https://github.com/ZeroOne3010/yetanotherhueapi) library.