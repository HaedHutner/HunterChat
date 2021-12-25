# HunterChat
A chat plugin for the Hunter plugin suite

## Permissions

### Command permissions

See commands section

### Channel permissions

Each channel has a permission setting, this forms the base of the channels permissions.
This defaults to "hunterchat.channels.<channel-id>"

| Permission | Description |
|---|---|
| .read | Permission to receive messages from the channel |
| .speak | Permission to send messages to the channel |
| .leave | Permission to leave the specific channel |
| .format | Permission to send formatted messages |

## Commands

| Command | Description | Permission |
|---|---|---|
| /chat | Lists the players current channels | hunterchat.commands |
| /chat join \<channel\> | Joins a specific channel | hunterchat.commands.join |
| /chat leave \<channel\> | Leaves a specific channel | hunterchat.commands.leave |
| /chat say \<channel\> \<message\> | Sends a message directly to a channel | hunterchat.commands.speak |

## Chat Configuration

### Main configuration

| Config Item | Description |
|---|---|
| default-channel | The default channel that players will speak too when they join the server |
| auto-join-channels | A list of channels for the player to automatically join |
| channels | A list of Channel configs |

### Channel Configuation

Channel configurations are provided as a map of channel-ids to channel configurations

| Config Item | Description |
|---|---|
| name | Display name of the channel, supports formatting codes |
| permission | Defaults to "hunterchat.channels.<channel-id>" |
| format | How to format messages sent to this channel |
|        | Defaults to "%cprefix %player: %message %csuffix"
|        | Supports colour codes and variables
| prefix | Prefix of the channel |
| suffix | Suffix of the Channel |
| aliases | List of command aliases to speak directly to this channel |
| type | **Broadcast** - All online members regardless of membership |
|      | **Global** - All online players in channel |
|      | **World** - All players in channel, in the same world |
|      | **Range** - All players in channel, within range |
| range | Radius for ranged channels |

#### Formatting Variables

The format of channels supports a number of variables:

| Variable | Description
|---|---|
| %prefix | Prefix of the sender |
| %suffix | Suffix of the sender |
| %cprefix | Prefix of the channel |
| %csuffix | Suffix of the channel |
| %player | The sender name |
| %message | The message |
| %world | The name of the senders world |
