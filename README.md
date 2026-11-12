# MeguminStaffAddon Minecraft Plugin

MeguminStaffAddon is a Paper Minecraft plugin that adds a custom Megumin's Explosion staff, including a staff item, player command, right-click casting, explosion effects, sounds, cooldown handling, and permission control.

## Install

1. Install Java 21.
2. Install a Paper Minecraft server compatible with Minecraft 1.21.
3. Build the plugin with Maven:

   ```bash
   mvn package
   ```

4. Copy `target/plugin.jar` into the server's `plugins` folder.
5. Start or restart the Paper server.
6. Confirm that the plugin enabled successfully in the server console.

## Use

1. Join the server as an operator or use a player with the `meguminstaff.give` permission.
2. Run:

   ```text
   /meguminstaff
   ```

3. Hold the received Megumin's Explosion Staff.
4. Right-click the air or a block to cast the explosion.
5. The explosion has a power of 6, breaks blocks, and does not create fire.
6. Wait 30 seconds before casting again. During the cooldown, the remaining time appears in the player's action bar.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/3caa7279-3c2d-4310-9a32-0f3e012b4821?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=3caa7279-3c2d-4310-9a32-0f3e012b4821
