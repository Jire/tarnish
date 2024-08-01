package com.osroyale.util.tools.wiki.impl;

import com.osroyale.util.tools.wiki.parser.WikiTable;
import com.osroyale.util.tools.wiki.parser.WikiTableParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;

public class NpcDefParser extends WikiTableParser  {

    public NpcDefParser() {
        super(generateTables());
    }

    private static LinkedList<WikiTable> generateTables() {
        LinkedList<WikiTable> tables = new LinkedList<>();
        tables.add(new WikiTable("http://oldschoolrunescape.wikia.com/wiki/King_Black_Dragon") {
            @Override
            protected void parseDocument(Document document) {
                Elements infobox = document.select(".wikitable.infobox");

                Elements desc = infobox.select("td");
                System.out.println(desc.tagName("text-align:center").text());
                for (Element child : desc) {
                    System.out.println(child.text());
                }
            }
        });
        return tables;
    }

    @Override
    protected void finish() {

    }

    public static void main(String[] args) throws InterruptedException {
        NpcDefParser parser = new NpcDefParser();
        parser.begin();
    }

    /*

    {{Infobox Monster
    |name =
    |image =
    |release =
    |update =
    |members =
    |combat =
    |hitpoints =
    |slaylvl =
    |slayexp =
    |aggressive =
    |poisonous =
    |attack speed =
    |max hit =
    |weakness =
    |always drops =
    |examine =
    |attack style =
    |cat =
    |turael =
    |mazchna =
    |vannaka =
    |chaeldar =
    |nieve =
    |duradel =
    |immunepoison =
    |immunevenom =
    |att =
    |str =
    |def =
    |mage =
    |range =
    |astab =
    |aslash =
    |acrush =
    |amagic =
    |arange =
    |dstab =
    |dslash =
    |dcrush =
    |dmagic =
    |drange =
    |strbns =
    |rngbns =
    |attbns =
    }}

     */


    /*

<table class="wikitable infobox">
 <caption>
   King Black Dragon
 </caption>
 <tbody>
  <tr>
   <td colspan="20" style="text-align:center;"> <a href="https://vignette.wikia.nocookie.net/2007scape/images/e/e9/King_Black_Dragon.png/revision/latest?cb=20160122062324" class="image image-thumbnail"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="King Black Dragon" class="lzy lzyPlcHld " data-image-key="King_Black_Dragon.png" data-image-name="King Black Dragon.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/e/e9/King_Black_Dragon.png/revision/latest/scale-to-width-down/300?cb=20160122062324" width="300" height="216" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
     <noscript>
      <img src="https://vignette.wikia.nocookie.net/2007scape/images/e/e9/King_Black_Dragon.png/revision/latest/scale-to-width-down/300?cb=20160122062324" alt="King Black Dragon" class="" data-image-key="King_Black_Dragon.png" data-image-name="King Black Dragon.png" width="300" height="216">
     </noscript></a> </td>
  </tr>
  <tr>
   <th colspan="8"> Also called </th>
   <td colspan="12">KBD </td>
  </tr>
  <tr>
   <th colspan="8"> Release date </th>
   <td colspan="12"> 24 September <a href="/wiki/2002" title="2002">2002</a> (<a href="/wiki/Update:Tutorial_island" title="Update:Tutorial island">Update</a>) </td>
  </tr>
  <tr>
   <th colspan="8"> <a href="/wiki/Members" title="Members">Members</a>? </th>
   <td colspan="12"> Yes </td>
  </tr>
  <tr>
   <th colspan="8"> <a href="/wiki/Combat_level" title="Combat level">Combat level</a> </th>
   <td colspan="12"> 276 </td>
  </tr>
  <tr>
   <th colspan="8"> Always <a href="/wiki/Drops" title="Drops">drops</a> </th>
   <td colspan="12"><a href="/wiki/Dragon_bones" title="Dragon bones">Dragon bones</a>, 2x <a href="/wiki/Black_dragonhide" title="Black dragonhide">Black dragonhide</a> </td>
  </tr>
  <tr>
   <th colspan="20"> <a href="/wiki/Examine" title="Examine">Examine</a> </th>
  </tr>
  <tr style="text-align:center;">
   <td colspan="20" style="padding:3px 7px 3px 7px; line-height:140%; text-align:center;"> The biggest, meanest dragon around. </td>
  </tr>
  <tr>
   <td style="width:100%;padding:0" colspan="15">
    <table class="wikitable mw-collapsible" style="width:100%; min-width:263px; text-align:center;" data-expandtext="show" data-collapsetext="hide">
     <tbody>
      <tr>
       <th colspan="20"> <a href="/wiki/Combat" title="Combat">Combat</a> info </th>
      </tr>
      <tr>
       <th colspan="8"> <a href="/wiki/Hitpoints" title="Hitpoints">Hitpoints</a> </th>
       <td colspan="12"> 240 </td>
      </tr>
      <tr>
       <th colspan="8"> <a href="/wiki/Aggressiveness" title="Aggressiveness">Aggressive</a> </th>
       <td colspan="12"> Yes </td>
      </tr>
      <tr>
       <th colspan="8"> <a href="/wiki/Poison" title="Poison">Poisonous</a> </th>
       <td colspan="12"> Yes </td>
      </tr>
      <tr>
       <th colspan="8"> <a href="/wiki/Monster_maximum_hit" title="Monster maximum hit">Max hit</a> </th>
       <td colspan="12"> 25 (<a href="/wiki/Melee" title="Melee">Melee</a>), 65 (<a href="/wiki/Dragonfire" title="Dragonfire">Dragonfire</a>) </td>
      </tr>
      <tr>
       <th colspan="8"> Weakness </th>
       <td colspan="12"> <a href="/wiki/Ranged" title="Ranged">Ranged</a>, <a href="/wiki/Stab" title="Stab" class="mw-redirect">Stab</a> </td>
      </tr>
      <tr>
       <th colspan="20"> <a href="/wiki/Attack_Style" title="Attack Style" class="mw-redirect">Attack Styles</a> </th>
      </tr>
      <tr style="text-align:center;">
       <td colspan="20"> <a href="/wiki/Melee" title="Melee">Melee</a> (<a href="/wiki/Slash" title="Slash" class="mw-redirect">Slash</a>), <a href="/wiki/Dragonfire" title="Dragonfire">Dragonfire</a> (long-ranged) </td>
      </tr>
      <tr>
       <th colspan="20"> <span class="plink-template"><a href="/wiki/Slayer" class="image image-thumbnail link-internal" title="Slayer"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Slayer-icon" class="lzy lzyPlcHld " data-image-key="Slayer-icon.png" data-image-name="Slayer-icon.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/c/cf/Slayer-icon.png/revision/latest?cb=20141020205814" width="23" height="24" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
          <noscript>
           <img src="https://vignette.wikia.nocookie.net/2007scape/images/c/cf/Slayer-icon.png/revision/latest?cb=20141020205814" alt="Slayer-icon" class="" data-image-key="Slayer-icon.png" data-image-name="Slayer-icon.png" width="23" height="24">
          </noscript></a>&nbsp;<a href="/wiki/Slayer" title="Slayer">Slayer info</a></span> </th>
      </tr>
      <tr>
       <th colspan="8" style="white-space: nowrap;"> <a href="/wiki/Slayer" title="Slayer">Slayer</a> level </th>
       <td colspan="12"> 1 </td>
      </tr>
      <tr>
       <th colspan="8" style="white-space: nowrap;"> <a href="/wiki/Slayer" title="Slayer">Slayer</a> <a href="/wiki/XP" title="XP" class="mw-redirect">XP</a> </th>
       <td colspan="12"> 258 </td>
      </tr>
      <tr>
       <th colspan="8" style="white-space: nowrap;"> <a href="/wiki/Slayer_assignment#List_of_assignments" title="Slayer assignment">Category</a> </th>
       <td colspan="12"> Black dragon, Boss </td>
      </tr>
      <tr>
       <th colspan="20" style="white-space: nowrap;"> <a href="/wiki/Slayer_master" title="Slayer master">Assigned by</a> </th>
      </tr>
      <tr style="text-align:center;">
       <td colspan="20"> <a href="/wiki/Krystilia" class="image image-thumbnail link-internal" title="Krystilia"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Krystilia chathead" class="lzy lzyPlcHld " data-image-key="Krystilia_chathead.png" data-image-name="Krystilia chathead.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/2/24/Krystilia_chathead.png/revision/latest/scale-to-width-down/45?cb=20170413111854" width="45" height="40" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
         <noscript>
          <img src="https://vignette.wikia.nocookie.net/2007scape/images/2/24/Krystilia_chathead.png/revision/latest/scale-to-width-down/45?cb=20170413111854" alt="Krystilia chathead" class="" data-image-key="Krystilia_chathead.png" data-image-name="Krystilia chathead.png" width="45" height="40">
         </noscript></a><a href="/wiki/Nieve" class="image image-thumbnail link-internal" title="Nieve"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Nieve chathead" class="lzy lzyPlcHld " data-image-key="Nieve_chathead.png" data-image-name="Nieve chathead.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/5/50/Nieve_chathead.png/revision/latest/scale-to-width-down/31?cb=20131212200502" width="31" height="40" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
         <noscript>
          <img src="https://vignette.wikia.nocookie.net/2007scape/images/5/50/Nieve_chathead.png/revision/latest/scale-to-width-down/31?cb=20131212200502" alt="Nieve chathead" class="" data-image-key="Nieve_chathead.png" data-image-name="Nieve chathead.png" width="31" height="40">
         </noscript></a><a href="/wiki/Duradel" class="image image-thumbnail link-internal" title="Duradel"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Duradel chathead" class="lzy lzyPlcHld " data-image-key="Duradel_chathead.png" data-image-name="Duradel chathead.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/1/13/Duradel_chathead.png/revision/latest/scale-to-width-down/31?cb=20131213091035" width="31" height="40" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
         <noscript>
          <img src="https://vignette.wikia.nocookie.net/2007scape/images/1/13/Duradel_chathead.png/revision/latest/scale-to-width-down/31?cb=20131213091035" alt="Duradel chathead" class="" data-image-key="Duradel_chathead.png" data-image-name="Duradel chathead.png" width="31" height="40">
         </noscript></a> </td>
      </tr>
      <tr>
       <th colspan="20"> <span class="plink-template"><a href="/wiki/Combat" class="image image-thumbnail link-internal" title="Combat"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Combat-icon" class="lzy lzyPlcHld " data-image-key="Combat-icon.png" data-image-name="Combat-icon.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/8/85/Combat-icon.png/revision/latest?cb=20151110012159" width="19" height="19" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
          <noscript>
           <img src="https://vignette.wikia.nocookie.net/2007scape/images/8/85/Combat-icon.png/revision/latest?cb=20151110012159" alt="Combat-icon" class="" data-image-key="Combat-icon.png" data-image-name="Combat-icon.png" width="19" height="19">
          </noscript></a>&nbsp;<a href="/wiki/Combat" title="Combat">Combat stats</a></span> </th>
      </tr>
      <tr>
       <th colspan="4"> <span class="SkillClickPic"><a href="/wiki/Attack" class="image image-thumbnail link-internal" title="Attack"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Attack-icon" class="lzy lzyPlcHld " data-image-key="Attack-icon.png" data-image-name="Attack-icon.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/5/51/Attack-icon.png/revision/latest/scale-to-width-down/20?cb=20130227091657" width="20" height="21" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
          <noscript>
           <img src="https://vignette.wikia.nocookie.net/2007scape/images/5/51/Attack-icon.png/revision/latest/scale-to-width-down/20?cb=20130227091657" alt="Attack-icon" class="" data-image-key="Attack-icon.png" data-image-name="Attack-icon.png" width="20" height="21">
          </noscript></a></span> </th>
       <th colspan="4"> <span class="SkillClickPic"><a href="/wiki/Strength" class="image image-thumbnail link-internal" title="Strength"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Strength-icon" class="lzy lzyPlcHld " data-image-key="Strength-icon.png" data-image-name="Strength-icon.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/3/3e/Strength-icon.png/revision/latest?cb=20141020205919" width="17" height="21" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
          <noscript>
           <img src="https://vignette.wikia.nocookie.net/2007scape/images/3/3e/Strength-icon.png/revision/latest?cb=20141020205919" alt="Strength-icon" class="" data-image-key="Strength-icon.png" data-image-name="Strength-icon.png" width="17" height="21">
          </noscript></a></span> </th>
       <th colspan="4"> <span class="SkillClickPic"><a href="/wiki/Defence" class="image image-thumbnail link-internal" title="Defence"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Defence-icon" class="lzy lzyPlcHld " data-image-key="Defence-icon.png" data-image-name="Defence-icon.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/d/d8/Defence-icon.png/revision/latest?cb=20141020204958" width="19" height="21" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
          <noscript>
           <img src="https://vignette.wikia.nocookie.net/2007scape/images/d/d8/Defence-icon.png/revision/latest?cb=20141020204958" alt="Defence-icon" class="" data-image-key="Defence-icon.png" data-image-name="Defence-icon.png" width="19" height="21">
          </noscript></a></span> </th>
       <th colspan="4"> <span class="SkillClickPic"><a href="/wiki/Ranged" class="image image-thumbnail link-internal" title="Ranged"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Ranged-icon" class="lzy lzyPlcHld " data-image-key="Ranged-icon.png" data-image-name="Ranged-icon.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/7/72/Ranged-icon.png/revision/latest/scale-to-width-down/21?cb=20141020205407" width="21" height="21" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
          <noscript>
           <img src="https://vignette.wikia.nocookie.net/2007scape/images/7/72/Ranged-icon.png/revision/latest/scale-to-width-down/21?cb=20141020205407" alt="Ranged-icon" class="" data-image-key="Ranged-icon.png" data-image-name="Ranged-icon.png" width="21" height="21">
          </noscript></a></span> </th>
       <th colspan="4"> <span class="SkillClickPic"><a href="/wiki/Magic" class="image image-thumbnail link-internal" title="Magic"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Magic-icon" class="lzy lzyPlcHld " data-image-key="Magic-icon.png" data-image-name="Magic-icon.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/7/77/Magic-icon.png/revision/latest/scale-to-width-down/21?cb=20141020205226" width="21" height="19" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
          <noscript>
           <img src="https://vignette.wikia.nocookie.net/2007scape/images/7/77/Magic-icon.png/revision/latest/scale-to-width-down/21?cb=20141020205226" alt="Magic-icon" class="" data-image-key="Magic-icon.png" data-image-name="Magic-icon.png" width="21" height="19">
          </noscript></a></span> </th>
      </tr>
      <tr style="text-align:center;">
       <td style="width:20%;" colspan="4"> 240 </td>
       <td style="width:20%;" colspan="4"> 240 </td>
       <td style="width:20%;" colspan="4"> 240 </td>
       <td style="width:20%;" colspan="4"> 1 </td>
       <td style="width:20%;" colspan="4"> 240 </td>
      </tr>
      <tr>
       <th colspan="20"> <span class="plink-template"><a href="/wiki/Attack" class="image image-thumbnail link-internal" title="Attack"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Attack-icon" class="lzy lzyPlcHld " data-image-key="Attack-icon.png" data-image-name="Attack-icon.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/5/51/Attack-icon.png/revision/latest?cb=20130227091657" width="25" height="26" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
          <noscript>
           <img src="https://vignette.wikia.nocookie.net/2007scape/images/5/51/Attack-icon.png/revision/latest?cb=20130227091657" alt="Attack-icon" class="" data-image-key="Attack-icon.png" data-image-name="Attack-icon.png" width="25" height="26">
          </noscript></a>&nbsp;<a href="/wiki/Attack" title="Attack">Aggressive stats</a></span> </th>
      </tr>
      <tr>
       <th colspan="4"> <a href="/wiki/Stab" class="image image-thumbnail link-internal" title="Stab"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="White dagger" class="lzy lzyPlcHld " data-image-key="White_dagger.png" data-image-name="White dagger.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/5/5c/White_dagger.png/revision/latest?cb=20130227205219" width="21" height="31" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
         <noscript>
          <img src="https://vignette.wikia.nocookie.net/2007scape/images/5/5c/White_dagger.png/revision/latest?cb=20130227205219" alt="White dagger" class="" data-image-key="White_dagger.png" data-image-name="White dagger.png" width="21" height="31">
         </noscript></a> </th>
       <th colspan="4"> <a href="/wiki/Slash" class="image image-thumbnail link-internal" title="Slash"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="White scimitar" class="lzy lzyPlcHld " data-image-key="White_scimitar.png" data-image-name="White scimitar.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/8/8b/White_scimitar.png/revision/latest?cb=20130227205337" width="27" height="30" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
         <noscript>
          <img src="https://vignette.wikia.nocookie.net/2007scape/images/8/8b/White_scimitar.png/revision/latest?cb=20130227205337" alt="White scimitar" class="" data-image-key="White_scimitar.png" data-image-name="White scimitar.png" width="27" height="30">
         </noscript></a> </th>
       <th colspan="4"> <a href="/wiki/Crush" class="image image-thumbnail link-internal" title="Crush"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="White warhammer" class="lzy lzyPlcHld " data-image-key="White_warhammer.png" data-image-name="White warhammer.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/6/6a/White_warhammer.png/revision/latest?cb=20130227204528" width="22" height="29" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
         <noscript>
          <img src="https://vignette.wikia.nocookie.net/2007scape/images/6/6a/White_warhammer.png/revision/latest?cb=20130227204528" alt="White warhammer" class="" data-image-key="White_warhammer.png" data-image-name="White warhammer.png" width="22" height="29">
         </noscript></a> </th>
       <th colspan="4"> <a href="/wiki/Magic" class="image image-thumbnail link-internal" title="Magic"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Magic-icon" class="lzy lzyPlcHld " data-image-key="Magic-icon.png" data-image-name="Magic-icon.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/7/77/Magic-icon.png/revision/latest?cb=20141020205226" width="25" height="23" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
         <noscript>
          <img src="https://vignette.wikia.nocookie.net/2007scape/images/7/77/Magic-icon.png/revision/latest?cb=20141020205226" alt="Magic-icon" class="" data-image-key="Magic-icon.png" data-image-name="Magic-icon.png" width="25" height="23">
         </noscript></a> </th>
       <th colspan="4"> <a href="/wiki/Ranged" class="image image-thumbnail link-internal" title="Ranged"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Ranged-icon" class="lzy lzyPlcHld " data-image-key="Ranged-icon.png" data-image-name="Ranged-icon.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/7/72/Ranged-icon.png/revision/latest?cb=20141020205407" width="23" height="23" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
         <noscript>
          <img src="https://vignette.wikia.nocookie.net/2007scape/images/7/72/Ranged-icon.png/revision/latest?cb=20141020205407" alt="Ranged-icon" class="" data-image-key="Ranged-icon.png" data-image-name="Ranged-icon.png" width="23" height="23">
         </noscript></a> </th>
      </tr>
      <tr style="text-align:center;">
       <td colspan="4"> +0 </td>
       <td colspan="4"> +0 </td>
       <td colspan="4"> +0 </td>
       <td colspan="4"> +0 </td>
       <td colspan="4"> +0 </td>
      </tr>
      <tr>
       <th colspan="20"> <span class="plink-template"><a href="/wiki/Defence" class="image image-thumbnail link-internal" title="Defence"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Defence-icon" class="lzy lzyPlcHld " data-image-key="Defence-icon.png" data-image-name="Defence-icon.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/d/d8/Defence-icon.png/revision/latest?cb=20141020204958" width="17" height="19" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
          <noscript>
           <img src="https://vignette.wikia.nocookie.net/2007scape/images/d/d8/Defence-icon.png/revision/latest?cb=20141020204958" alt="Defence-icon" class="" data-image-key="Defence-icon.png" data-image-name="Defence-icon.png" width="17" height="19">
          </noscript></a>&nbsp;<a href="/wiki/Defence" title="Defence">Defensive stats</a></span> </th>
      </tr>
      <tr>
       <th colspan="4"> <a href="/wiki/Stab" class="image image-thumbnail link-internal" title="Stab"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="White dagger" class="lzy lzyPlcHld " data-image-key="White_dagger.png" data-image-name="White dagger.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/5/5c/White_dagger.png/revision/latest?cb=20130227205219" width="21" height="31" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
         <noscript>
          <img src="https://vignette.wikia.nocookie.net/2007scape/images/5/5c/White_dagger.png/revision/latest?cb=20130227205219" alt="White dagger" class="" data-image-key="White_dagger.png" data-image-name="White dagger.png" width="21" height="31">
         </noscript></a> </th>
       <th colspan="4"> <a href="/wiki/Slash" class="image image-thumbnail link-internal" title="Slash"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="White scimitar" class="lzy lzyPlcHld " data-image-key="White_scimitar.png" data-image-name="White scimitar.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/8/8b/White_scimitar.png/revision/latest?cb=20130227205337" width="27" height="30" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
         <noscript>
          <img src="https://vignette.wikia.nocookie.net/2007scape/images/8/8b/White_scimitar.png/revision/latest?cb=20130227205337" alt="White scimitar" class="" data-image-key="White_scimitar.png" data-image-name="White scimitar.png" width="27" height="30">
         </noscript></a> </th>
       <th colspan="4"> <a href="/wiki/Crush" class="image image-thumbnail link-internal" title="Crush"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="White warhammer" class="lzy lzyPlcHld " data-image-key="White_warhammer.png" data-image-name="White warhammer.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/6/6a/White_warhammer.png/revision/latest?cb=20130227204528" width="22" height="29" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
         <noscript>
          <img src="https://vignette.wikia.nocookie.net/2007scape/images/6/6a/White_warhammer.png/revision/latest?cb=20130227204528" alt="White warhammer" class="" data-image-key="White_warhammer.png" data-image-name="White warhammer.png" width="22" height="29">
         </noscript></a> </th>
       <th colspan="4"> <a href="/wiki/Magic" class="image image-thumbnail link-internal" title="Magic"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Magic-icon" class="lzy lzyPlcHld " data-image-key="Magic-icon.png" data-image-name="Magic-icon.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/7/77/Magic-icon.png/revision/latest?cb=20141020205226" width="25" height="23" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
         <noscript>
          <img src="https://vignette.wikia.nocookie.net/2007scape/images/7/77/Magic-icon.png/revision/latest?cb=20141020205226" alt="Magic-icon" class="" data-image-key="Magic-icon.png" data-image-name="Magic-icon.png" width="25" height="23">
         </noscript></a> </th>
       <th colspan="4"> <a href="/wiki/Ranged" class="image image-thumbnail link-internal" title="Ranged"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Ranged-icon" class="lzy lzyPlcHld " data-image-key="Ranged-icon.png" data-image-name="Ranged-icon.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/7/72/Ranged-icon.png/revision/latest?cb=20141020205407" width="23" height="23" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
         <noscript>
          <img src="https://vignette.wikia.nocookie.net/2007scape/images/7/72/Ranged-icon.png/revision/latest?cb=20141020205407" alt="Ranged-icon" class="" data-image-key="Ranged-icon.png" data-image-name="Ranged-icon.png" width="23" height="23">
         </noscript></a> </th>
      </tr>
      <tr style="text-align:center;">
       <td colspan="4"> +70 </td>
       <td colspan="4"> +90 </td>
       <td colspan="4"> +90 </td>
       <td colspan="4"> +80 </td>
       <td colspan="4"> +70 </td>
      </tr>
      <tr>
       <th colspan="12"> Other bonuses </th>
       <th colspan="12"> Immunities </th>
      </tr>
      <tr>
       <th colspan="4"> <a href="/wiki/Strength" class="image image-thumbnail link-internal" title="Monster's strength bonus"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Strength-icon" class="lzy lzyPlcHld " data-image-key="Strength-icon.png" data-image-name="Strength-icon.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/3/3e/Strength-icon.png/revision/latest?cb=20141020205919" width="16" height="20" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
         <noscript>
          <img src="https://vignette.wikia.nocookie.net/2007scape/images/3/3e/Strength-icon.png/revision/latest?cb=20141020205919" alt="Strength-icon" class="" data-image-key="Strength-icon.png" data-image-name="Strength-icon.png" width="16" height="20">
         </noscript></a> </th>
       <th colspan="4"> <a href="/wiki/Ranged_Strength" class="image image-thumbnail link-internal" title="Monster's ranged strength bonus"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="RangedStrength-icon" class="lzy lzyPlcHld " data-image-key="RangedStrength-icon.png" data-image-name="RangedStrength-icon.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/f/fc/RangedStrength-icon.png/revision/latest?cb=20140731232105" width="26" height="25" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
         <noscript>
          <img src="https://vignette.wikia.nocookie.net/2007scape/images/f/fc/RangedStrength-icon.png/revision/latest?cb=20140731232105" alt="RangedStrength-icon" class="" data-image-key="RangedStrength-icon.png" data-image-name="RangedStrength-icon.png" width="26" height="25">
         </noscript></a> </th>
       <th colspan="4"> <a href="/wiki/Attack" class="image image-thumbnail link-internal" title="Monster's attack bonus"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Attack-icon" class="lzy lzyPlcHld " data-image-key="Attack-icon.png" data-image-name="Attack-icon.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/5/51/Attack-icon.png/revision/latest?cb=20130227091657" width="25" height="26" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
         <noscript>
          <img src="https://vignette.wikia.nocookie.net/2007scape/images/5/51/Attack-icon.png/revision/latest?cb=20130227091657" alt="Attack-icon" class="" data-image-key="Attack-icon.png" data-image-name="Attack-icon.png" width="25" height="26">
         </noscript></a> </th>
       <th colspan="4"> <a href="/wiki/Poison" class="image image-thumbnail link-internal" title="Poison"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Poison hitsplat" class="lzy lzyPlcHld " data-image-key="Poison_hitsplat.png" data-image-name="Poison hitsplat.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/c/ca/Poison_hitsplat.png/revision/latest?cb=20151109225928" width="24" height="23" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
         <noscript>
          <img src="https://vignette.wikia.nocookie.net/2007scape/images/c/ca/Poison_hitsplat.png/revision/latest?cb=20151109225928" alt="Poison hitsplat" class="" data-image-key="Poison_hitsplat.png" data-image-name="Poison hitsplat.png" width="24" height="23">
         </noscript></a> </th>
       <th colspan="4"> <a href="/wiki/Venom" class="image image-thumbnail link-internal" title="Venom"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Venom hitsplat" class="lzy lzyPlcHld " data-image-key="Venom_hitsplat.png" data-image-name="Venom hitsplat.png" data-src="https://vignette.wikia.nocookie.net/2007scape/images/3/37/Venom_hitsplat.png/revision/latest?cb=20151109230630" width="24" height="23" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
         <noscript>
          <img src="https://vignette.wikia.nocookie.net/2007scape/images/3/37/Venom_hitsplat.png/revision/latest?cb=20151109230630" alt="Venom hitsplat" class="" data-image-key="Venom_hitsplat.png" data-image-name="Venom hitsplat.png" width="24" height="23">
         </noscript></a> </th>
      </tr>
      <tr style="text-align:center;">
       <td colspan="4"> +0 </td>
       <td colspan="4"> +0 </td>
       <td colspan="4"> +0 </td>
       <td colspan="4"> Not immune </td>
       <td colspan="4"> Not immune </td>
      </tr>
      <tr>
       <th colspan="20" style="white-space: nowrap;"> <a href="/wiki/Monster_attack_speed" title="Monster attack speed">Attack speed</a> </th>
      </tr>
      <tr>
       <th colspan="20" style="height: 55px;"> <span style="font-size:12px;font-family:sans-serif;font-weight:bold;line-height:90%;margin:0;padding:0;height:46px;width:170px;display:inline-block;text-align:center;"><img src="data:image/gif;base64,R0lGODlhAQABAIABAAAAAP///yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D" alt="Monster attack speed 7" class="lzy lzyPlcHld " data-image-key="Monster_attack_speed_7.gif" data-image-name="Monster attack speed 7.gif" data-src="https://vignette.wikia.nocookie.net/2007scape/images/7/72/Monster_attack_speed_7.gif/revision/latest?cb=20130301182348" width="164" height="46" onload="if(typeof ImgLzy==='object'){ImgLzy.load(this)}">
         <noscript>
          <img src="https://vignette.wikia.nocookie.net/2007scape/images/7/72/Monster_attack_speed_7.gif/revision/latest?cb=20130301182348" alt="Monster attack speed 7" class="" data-image-key="Monster_attack_speed_7.gif" data-image-name="Monster attack speed 7.gif" width="164" height="46">
         </noscript></span> </th>
      </tr>
     </tbody>
    </table> </td>
  </tr>
 </tbody>
</table>

     */

}
