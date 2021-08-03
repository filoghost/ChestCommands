ChestCommands - Fork with ItemsAdder compatibility
===================

## ChestCommands
Bukkit Page: http://dev.bukkit.org/projects/chest-commands

API Javadoc for developers: https://ci.codemc.io/job/filoghost/job/ChestCommands/javadoc/index.html?me/filoghost/chestcommands/api/ChestCommandsAPI.html

## Maven
```xml
<repository>
    <id>codemc-repo</id>
    <url>https://repo.codemc.io/repository/maven-public/</url>
</repository>
```

```xml
<dependency>
    <groupId>me.filoghost.chestcommands</groupId>
    <artifactId>chestcommands-api</artifactId>
    <version>4.0.2</version>
    <scope>provided</scope>
</dependency>
```

## License
Chest Commands is free software/open source, and is distributed under the [GPL 3.0 License](https://opensource.org/licenses/GPL-3.0). It contains third-party code, see the included THIRD-PARTY.txt file for the license information on third-party code.

## ItemsAdder
Spigot Page: https://www.spigotmc.org/resources/73355/


## Example of what you can achieve
![](https://i.imgur.com/wuFEAoZ.gif)


welcome.yml
```YAML
menu-settings:
  name: ':offset_-16::welcome_menu::offset_-192:'
  rows: 6
  auto-reopen: true
  commands:
  - welcome
accept1:
  ACTIONS:
  - warp survival
  - "close-this:"
  NAME: '&aAccept'
  LORE:
  - /home
  BLANK: true
  POSITION-X: 3
  POSITION-Y: 5
accept2:
  ACTIONS:
  - warp survival
  - "close-this:"
  NAME: '&aAccept'
  LORE:
  - /home
  BLANK: true
  POSITION-X: 4
  POSITION-Y: 5
cancel1:
  ACTIONS:
  - "console: kick {player} You have to accept the rules to enter the server!"
  NAME: '&cLeave the server'
  LORE:
  - /home
  BLANK: true
  POSITION-X: 6
  POSITION-Y: 5
cancel2:
  ACTIONS:
  - "console: kick {player} You have to accept the rules to enter the server!"
  NAME: '&cLeave the server'
  LORE:
  - /home
  BLANK: true
  POSITION-X: 7
  POSITION-Y: 5

```

welcome_accepted.yml

```YAML
menu-settings:
  name: ':offset_-16::welcome_accepted::offset_-192:'
  rows: 6
  auto-close: 15
  commands:
  - welcome1
```
