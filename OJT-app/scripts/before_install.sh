#!/bin/bash
sudo rm -rf /opt/codedeploy/deployment-root/*
sudo rm -rf /home/ec2-user/*.jar /home/ec2-user/*.yml /home/ec2-user/*.sh
cd /home/ec2-user/
curl --silent --location https://rpm.nodesource.com/setup_12.x | bash -
yum -y install nodejs
