#!/usr/bin/env bash
authKey=""
valuesDir="src/main/res/values"
languages="de"
defaultLanguage="de"

# Main string files
for language in $languages
do
    if [ "$language" == $defaultLanguage ]; then
        saveDir="$valuesDir"
    else
        saveDir="$valuesDir-$language"
    fi

    mkdir -p $saveDir

    curl "https://localise.biz/api/export/locale/$language.xml?key=$authKey&format=android&fallback=$defaultLanguage" > "$saveDir/strings.xml"
done