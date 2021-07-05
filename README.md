# random-file-util

Desktop and command line app for randomly opening files. It is intended for opening random media files. The files are opened with default associated applications, as if you opened them from any file explorer.

## Desktop mode

Start with `java -jar randomfileutil.jar -gui -dir <target directory>`.

The app will find and open a random file from target directory or any subdirectories, then wait for next command with a GUI window.

## Command line mode

Start with `java -jar randomfileutil.jar -dir <target directory>`.

The app will find and open a random file from target directory or any subdirectories, then wait for next command.

```
[main] INFO io.kall.randomfileutil.RandomFileUtil - Opening file C:\temp\IMG_0050.jpg
| Last opened file
| Name:   IMG_0050.jpg
| Folder: C:\temp
| Size:   6 MB

Options:
  Q: Quit
  R: Open another random file
  N: Rename last opened file
  M: Move file to another folder
  D: Delete last opened file
  F: Open last file's folder
  S: Set filename search term
Select action [R]: 
```

## Ignored files

The random file selector ignores some files
 * by extension: .jar, .db, .exe, .bat, .cmd, .ini, .lnk, empty
 * by filename: folder.jpg