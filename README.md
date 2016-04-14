ToolsBox: A collection of useful things
=======================================

## 1. Arcbot: 

Is a simple tool which listens for "Origin" folder creation event files and for each new new file it will check 
if it is an image or a video and it will move it to the "Destination" folder organised by data creation time
in subfolders of year, month and day. It will use EFIX metadata information if it is available or the creation 
date instead.

By default the jar file folder it "$USER_HOME/apps/arcbot/current/toolsbox.jar". If a different location is chosen, 
please update the "src/main/resources/scripts/arcbot-xxx" service file.

There are two ways to run this tool. 

Directly run with the command as follows:
```
 Arguments:
 1 = origin folder to listen for create events
 2 = destination folder where to store files organised by subfolders year/month/day
 3 = log file name
 nohup java -jar toolsbox.jar $1 $2 > $3.log 2>&1 &
```

Or

Setup a background service by using the "src/main/resources/scripts/arcbot-xxx" script. 

1. Copy the script into /etc/init.d/
2. Edit the file and replace xxx by the instance name 
3. Setup the origin folder name locations

Notes:
The origin folder should be the folder used with 'btsync' to sync photos from a mobile device.
The destination folder only needs default ACL group permissions to enable 
enable admin tools of the Single File PHP Gallery (detailed below)
```
 Set ACL default folder group permissions
 sudo apt-get install acl
 sudo chmod 774 /..../FamilyPhotos
 getfacl -a /..../FamilyPhotos/ | setfacl -dR -M- /..../FamilyPhotos/
```


### 1.1 BtSync configuration:

Use these steps to setup Sync version 2.x.x
[Official btsync packages ](http://blog.bittorrent.com/2016/02/18/official-linux-packages-for-sync-now-available/)

For Arcbot setup

1. Add btsync group to the current user. 
```
sudo usermod -a -G pi btsync
```

2. Edit the Btsync config file "/etc/btsync/config.json" and edit lines as follows:
```
"webui" :
    {
        "listen" : "0.0.0.0:8888",
        "login" : "admin",
        "password" : "xxxx"
    }
NOTE: Add a password for the admin web GUI
```

3. Restart the btsync service
```
sudo service btsync restart
```

4. Open a browser window to "http://hostIpAddress:8888" . Enter user and password.


5. Install the mobile [Sync](https://www.getsync.com/platforms/mobile) application in the devices.

6. Link the mobile devices photo galleries to the "Origin" folder. The simple way is to get the folder key from the 
mobile device and add a new folder with the web GUI.

6.1 In the mobile Sync app open settings, Advanced settings and activate the swtich "Copy keys instead of links"
6.2 Go to Folders, Camera backup folder Devices and click button share to copy the Key
6.3 Send an email to oneself with the key
6.4 From the backup system machine, open a browser and load the email.
6.5 Copy key from email
6.6 Open the web GUI and Add the key with a folder location.


### 1.3 Single File PHP Gallery: 

From [SFPG](http://sye.dk/sfpg/) this is a simple and powerful PHP script to view the synced photos with the browser.
It is perfect as a light weight photo gallery tool.

In "src/main/resources/photoGallery" there is a copy of this script with some adaptations for this Arcbot solution.
All details for full detailed configuration is in the "src/main/resources/photoGallery"
  
For Arcbot and Btsync setup

  1. Create Symlink called "/images" towards the photos repository location and another one "_sfpg_data" 
  to a temporal folder with write permissions for the user www-data.
```  
     "ln -s /images /..../Photos"
     "ln -s /_sfpg_data /..../photoGalleryThumbs
```  
  2. Copy the "index.php" file to "/var/www/photos/"
  
  3. Add "www-data" user to current user group. i.e
```
   usermod -a -G pi www-data
```
  The current user needs at least read permissions for images folder.
  
  4. Edit "index.php" and add a security phrase.
```  
   define('SECURITY_PHRASE', 'xxxx');  
```

  5. Review the "src/main/resources/photoGallery" for further configuration.
```
  Example /var/www/photos configuration:
  lrwxrwxrwx 1 pi     www-data     34 Apr  9 23:26 images -> /..../Photos
  -rw-r--r-- 1 pi     www-data 107041 Apr  9 23:26 index.php
  -rw-r--r-- 1 pi     www-data  70462 Apr  9 23:26 readme.txt
  lrwxrwxrwx 1 pi     www-data     32 Apr  9 23:26 _sfpg_data -> /..../photoGalleryThumbs/
```

