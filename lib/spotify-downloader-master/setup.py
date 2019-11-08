from setuptools import setup

with open("README.md", "r", encoding="utf-8") as f:
    long_description = f.read()

import spotdl

setup(
    # 'spotify-downloader' was already taken :/
    name="spotdl",
    # Tests are included automatically:
    # https://docs.python.org/3.6/distutils/sourcedist.html#specifying-the-files-to-distribute
    packages=["spotdl", "spotdl.lyrics", "spotdl.lyrics.providers"],
    version=spotdl.__version__,
    install_requires=[
        "pathlib >= 1.0.1",
        "youtube_dl >= 2017.9.26",
        "pafy >= 0.5.3.1",
        "spotipy >= 2.4.4",
        "mutagen >= 1.41.1",
        "beautifulsoup4 >= 4.6.3",
        "unicode-slugify >= 0.1.3",
        "titlecase >= 0.10.0",
        "logzero >= 1.3.1",
        "lyricwikia >= 0.1.8",
        "PyYAML >= 3.13",
        "appdirs >= 1.4.3",
    ],
    description="Download songs from YouTube using Spotify song URLs or playlists with albumart and meta-tags.",
    long_description=long_description,
    long_description_content_type="text/markdown",
    author="Ritiek Malhotra and the spotify-downloader contributors",
    author_email="ritiekmalhotra123@gmail.com",
    license="MIT",
    python_requires=">=3.4",
    url="https://github.com/ritiek/spotify-downloader",
    download_url="https://pypi.org/project/spotdl/",
    keywords=[
        "spotify",
        "downloader",
        "download",
        "music",
        "youtube",
        "mp3",
        "album",
        "metadata",
    ],
    classifiers=[
        "Development Status :: 4 - Beta",
        "Intended Audience :: End Users/Desktop",
        "License :: OSI Approved :: MIT License",
        "Programming Language :: Python",
        "Programming Language :: Python :: 3",
        "Programming Language :: Python :: 3.4",
        "Programming Language :: Python :: 3.5",
        "Programming Language :: Python :: 3.6",
        "Programming Language :: Python :: 3.7",
        "Programming Language :: Python :: 3 :: Only",
        "Topic :: Multimedia",
        "Topic :: Multimedia :: Sound/Audio",
        "Topic :: Utilities",
    ],
    entry_points={"console_scripts": ["spotdl = spotdl.spotdl:main"]},
)
