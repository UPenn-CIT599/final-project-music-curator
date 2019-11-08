import os

from spotdl import const
from spotdl import downloader

import loader

loader.load_defaults()

TRACK_URL = "http://open.spotify.com/track/0JlS7BXXD07hRmevDnbPDU"


def test_dry_download_list(tmpdir):
    const.args.folder = str(tmpdir)
    const.args.dry_run = True
    file_path = os.path.join(const.args.folder, "test_list.txt")
    with open(file_path, "w") as f:
        f.write(TRACK_URL)
    list_dl = downloader.ListDownloader(file_path)
    downloaded_song, *_ = list_dl.download_list()
    assert downloaded_song == TRACK_URL
