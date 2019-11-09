package chronoMods.steam;

import chronoMods.*;
import chronoMods.steam.*;
import com.codedisaster.steamworks.*;
import com.megacrit.cardcrawl.screens.mainMenu.MenuButton;
import java.util.ArrayList;

public class SteamLobby {

	public SteamID steamID;

	public String name = "";
	public String owner = "MegaCrit";
	public String mode = "Versus";
	public String ascension = "0";
	public String character = "The Ironclad";
	public int capacity = 6;
	public int members = 0;

	public SteamID ownerID;

    public static ArrayList<RemotePlayer> players = new ArrayList();

	public SteamLobby (SteamID id) {
		this.steamID = id;

		try {
			name = NetworkHelper.matcher.getLobbyData(steamID, "name");
			mode = NetworkHelper.matcher.getLobbyData(steamID, "mode");
			ascension = NetworkHelper.matcher.getLobbyData(steamID, "ascension");
			character = NetworkHelper.matcher.getLobbyData(steamID, "character");
			ownerID = NetworkHelper.matcher.getLobbyOwner(this.steamID);
			getOwnerName();
			getMemberCount();

			// capacity = NetworkHelper.matcher.getLobbyMemberLimit(steamID);
		} catch (Exception e) {}
	}

	public String getOwnerName() {
		try {
			this.owner = NetworkHelper.friends.getFriendPersonaName(NetworkHelper.matcher.getLobbyOwner(this.steamID));
		} catch (Exception e) {}

		return this.owner;
	}

	public int getMemberCount() {
		try {
			this.members = NetworkHelper.matcher.getNumLobbyMembers(this.steamID);
		} catch (Exception e) {}

		return this.members;
	}

	public ArrayList<RemotePlayer> getLobbyMembers() {
		members = getMemberCount();
		players.clear();

		try {
			for (int i = 0; i < members; i++) {
				players.add(new RemotePlayer(NetworkHelper.matcher.getLobbyMemberByIndex(steamID, i)));
			}
		} catch (Exception e) {}

		return players;
	}
}