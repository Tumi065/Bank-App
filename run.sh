#!/bin/bash
cd "$(dirname "$0")"
export DISPLAY=:1

# install anything missing (only does work after a restart or rebuild)
if ! command -v Xvfb >/dev/null || ! command -v websockify >/dev/null || ! ldconfig -p | grep -q libgtk-3.so; then
  echo "Setting up (one-off after a reset)..."
  sudo apt-get update -qq
  sudo apt-get install -y openjfx libgtk-3-0 libgl1 libxtst6 libxxf86vm1 xvfb x11vnc novnc websockify fluxbox
fi

# start the virtual screen and viewer if they are not running
pgrep -x Xvfb >/dev/null      || { Xvfb :1 -screen 0 1280x800x24 >/dev/null 2>&1 & sleep 2; }
pgrep -x fluxbox >/dev/null   || { fluxbox >/dev/null 2>&1 & }
pgrep -x x11vnc >/dev/null    || { x11vnc -display :1 -nopw -forever -shared -rfbport 5900 >/dev/null 2>&1 & }
pgrep -f websockify >/dev/null || { websockify --web /usr/share/novnc 6080 localhost:5900 >/dev/null 2>&1 & }
sleep 2

# open the viewer tab, then start the app
URL="https://${CODESPACE_NAME}-6080.${GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN}/vnc.html?autoconnect=true&resize=scale"
echo "Viewer: $URL"
[ -n "$BROWSER" ] && "$BROWSER" "$URL" >/dev/null 2>&1

mvn -q javafx:run
