package de.netzkronehd.discordverifybot.manager;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.group.Group;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class VerifyManager extends Manager {

    private HashMap<UUID, String> requests;



    public VerifyManager(DiscordVerifyBot discordVerifyBot) {
        super(discordVerifyBot);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onReload() {
        onLoad();
    }

    public void verify(DiscordPlayer dp, Member member) {

    }

    public void updateVerification(DiscordPlayer dp) {
        if(dp.isVerified()) {
            final List<Group> permissionGroup = discordVerifyBot.getGroupManager().getGroups(dp);
            if(dp.getVerification().getMember() != null) {
                final List<Group> discordGroup = discordVerifyBot.getGroupManager().getGroups(dp.getVerification().getMember());

            } else {

            }



        } else throw new IllegalStateException("The player '"+dp.getName()+"' has to be verified to update the verification.");
    }

}
