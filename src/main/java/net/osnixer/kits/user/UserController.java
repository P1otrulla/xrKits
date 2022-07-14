package net.osnixer.kits.user;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;
import java.util.UUID;

public class UserController implements Listener {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        Optional<User> userOptional = this.userService.findUser(uniqueId);

        if (userOptional.isEmpty()) {
            this.userService.createUser(uniqueId);
        }
    }

    @EventHandler
    public void onQuit(PlayerKickEvent event) {
        Player player = event.getPlayer();

        this.userService.findUser(player.getUniqueId()).ifPresent(user -> {
            if (user.isChanged()) {
                this.userRepository.saveUser(user);
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        this.userService.findUser(player.getUniqueId()).ifPresent(this.userRepository::saveUser);
    }
}
