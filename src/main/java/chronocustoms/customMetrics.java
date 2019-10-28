package chronospeed;

import com.megacrit.cardcrawl.metrics.Metrics;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.screens.DeathScreen;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import basemod.ReflectionHacks;

import com.megacrit.cardcrawl.integrations.steam.SteamIntegration;
import com.codedisaster.steamworks.SteamApps;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamNativeHandle;

import chronospeed.*;

public class customMetrics implements Runnable {

  private HashMap<Object, Object> params = new HashMap();
  private Gson gson = new Gson();
  private long lastPlaytimeEnd;
  public static final SimpleDateFormat timestampFormatter = new SimpleDateFormat("yyyyMMddHHmmss");

  public static final String URL = "http://www.chronometry.ca/League/seasonfour/speedrun.php";


  private void addData(Object key, Object value)
  {
    this.params.put(key, value);
  }
  
  private void sendPost()
  {
    HashMap<String, Serializable> event = new HashMap();
    event.put("name", CardCrawlGame.playerName);
    event.put("alias", CardCrawlGame.alias);

    SteamApps steamApps = (SteamApps)ReflectionHacks.getPrivate(CardCrawlGame.publisherIntegration, SteamIntegration.class, "steamApps");
    SteamID id = steamApps.getAppOwner();
    long newid = (long)ReflectionHacks.getPrivate(id, SteamNativeHandle.class, "handle");

    event.put("steam", newid);
    event.put("character", AbstractDungeon.player.chosenClass.name());
    event.put("playtime", Float.valueOf(CardCrawlGame.playtime));

    String data = this.gson.toJson(event);
    HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
    
    Net.HttpRequest httpRequest = requestBuilder.newRequest().method("POST").url(URL).header("Content-Type", "application/json").header("Accept", "application/json").header("User-Agent", "curl/7.43.0").build();
    httpRequest.setContent(data);
    Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener()
    {
      public void handleHttpResponse(Net.HttpResponse httpResponse) {
      }
      
      public void failed(Throwable t) {
      }
      
      public void cancelled() {
      }
    });
  }
  
  public void run()
  {
    if (Settings.isStandardRun() && AbstractDungeon.deathScreen.isVictory) {
      sendPost();
    }
  }
}
