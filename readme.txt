Running command line examples
=============================

Mule in Action command line examples do not work with Mule 3's new container mode.

If you want to try them out, you need to enable the Mule 2-compatible bootstrap mechanism. For this, edit $MULE_HOME/conf/wrapper.conf file and replace the following line:

  # Java Main class
  wrapper.java.mainclass=org.mule.module.reboot.MuleContainerBootstrap

with this one:

  # Java Main class
  wrapper.java.mainclass=org.mule.module.boot.MuleBootstrap
