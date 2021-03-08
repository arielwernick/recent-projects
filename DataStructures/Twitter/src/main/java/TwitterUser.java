import java.util.LinkedList;

public class TwitterUser {
    String name;
    String hashTag;
    int followers;
    LinkedList<TwitterUser> accountsFollowing;
    public TwitterUser(String name, String hashtag){
        this.name = name;
        this.hashTag = hashTag;

    }

    public int newFollower(TwitterUser E){
        accountsFollowing.add(E);
        followers ++;
        return followers;
    }


}
