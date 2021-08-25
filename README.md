# Directory-Monitor
A simple directory polling app to monitor changes within a directory. Completely cross platform, it can monitor network directories too.

You can add several directories that you want to monitor, however this app would only monitor one directory at a time.

It would alert you of change in directory size, number of files, changes in files.

It would send you an email when the directory is about to fill up. If you want it to send email for every change in file simply call the sendEmail method from any of the conditions that fit in the checkDirectory method.

Now, note that:

The app checks the directory twice every second using java timer utility. For this reason this app MAY not be used for huge directories spanning millions of files.

This code runs on a single thread.

Check this Git for Super Directory-Monitor
This would work based on Java Watch Service and it would be multithreaded allowing to monitor multiple directories at a once.
