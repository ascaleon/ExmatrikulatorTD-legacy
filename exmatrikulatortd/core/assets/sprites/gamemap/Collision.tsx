<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.2" tiledversion="1.2.4" name="Collision" tilewidth="64" tileheight="64" tilecount="3" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0">
  <properties>
   <property name="buildableByPlayer" type="int" value="0"/>
  </properties>
  <image width="64" height="64" source="BuildableByPlayerTwo.png"/>
 </tile>
 <tile id="1">
  <properties>
   <property name="buildableByPlayer" type="int" value="-1"/>
  </properties>
  <image width="64" height="64" source="NotBuildable.png"/>
 </tile>
 <tile id="2">
  <properties>
   <property name="buildableByPlayer" type="int" value="1"/>
  </properties>
  <image width="64" height="64" source="BuildableByPlayerOne.png"/>
 </tile>
</tileset>
