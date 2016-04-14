ToolsBox: A collection of useful things
=======================================

Arcbot: 
-------
Is a simple tool which listens an Origin folder for events
 of creation files and for each new new file it will check it is an image
 or a video and it will move it to the Destination folder organised by
 subfolder of year, month and day. It will use EFIX metadata information
 if available or the creation date instead.

By default install arcbot at $USER_HOME/apps/arcbot/current/toolsbox.jar
If different location is choosen, please update the 
"src/main/resources/scripts/arcbot-xxx" service file.


Directly run with the command:

 Arguments:
 1 = origin folder to listen for create events
 2 = destination folder where to store files organised by subfolders year/month/day
 3 = log file name
nohup java -jar toolsbox.jar $1 $2 > $3.log 2>&1 &

Or

Setup a service by using the "src/main/resources/scripts/arcbot-xxx" script. 

1. Copy the script into /etc/init.d/
2. Edit the file and replace xxx by the instance name 
3. Setup the origin folder name locations

Notes:
The origin folder should be the folder used with 'btsync' to sync photos from a mobile device.
The destination folder only needs default ACL group permissions to enable 
enable admin tools of the Single File PHP Gallery (detailed below)

 Set ACL default folder group permissions
 sudo apt-get install acl
 sudo chmod 774 /..../FamilyPhotos
 getfacl -a /..../FamilyPhotos/ | setfacl -dR -M- /..../FamilyPhotos/



BtSync configuration:
---------------------
Use these steps to setup Sync version 2.x.x
http://blog.bittorrent.com/2016/02/18/official-linux-packages-for-sync-now-available/

1. Add btsync group to the <current user> "sudo usermod -a -G pi btsync"

2. Configure btsync to run as a service

3. Link mobile devices photo galleries to the Origin folder.



Single File PHP Gallery: 
------------------------
From http://sye.dk/sfpg/ this is a simple and powerful PHP script to 
view the synced photos. It is perfect as a light weight simple viewer tool.

In "src/main/resources/photoGallery" there is a copy of this script with 
some adaptations to this solution. All details for further configuration 
are in the readme.txt file in the "src/main/resources/photoGallery" folder.
  
Some configuration it is required to work with arcbot and btsync:

  1. Create Symlink called "/images" to the photos repository and
  another one "_sfpg_data" to a temporal folder with write permissions
  for the user www-data.
     "ln -s /images /..../Photos"
     "ln -s /_sfpg_data /..../photoGalleryThumbs
  
  2. Copy the "index.php" file to "/var/www/photos/"
  
  3. Add "www-data" user to <current user> group "usermod -a -G pi www-data"
  The <current user> needs at least read permissions for images folder.
  
  4. Edit "index.php" add a security phrase.
  
  5. Review the "readme.txt" for further configuration.

  Example /var/www/photos configuration:
  lrwxrwxrwx 1 pi     www-data     34 Apr  9 23:26 images -> /..../Photos
  -rw-r--r-- 1 pi     www-data 107041 Apr  9 23:26 index.php
  -rw-r--r-- 1 pi     www-data  70462 Apr  9 23:26 readme.txt
  lrwxrwxrwx 1 pi     www-data     32 Apr  9 23:26 _sfpg_data -> /..../photoGalleryThumbs/
