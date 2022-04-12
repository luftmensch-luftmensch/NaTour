# Convertitore in place da file GPX a json 
# Maggiore flessibilità di salvataggio dati in un nosql (mongoDB) che gestisce file json


#   .g8"""bgd `7MM"""Mq.`YMM'   `MP'       mm                   `7MMF'.M"""bgd   .g8""8q. `7MN.   `7MF'
# .dP'     `M   MM   `MM. VMb.  ,P         MM                     MM ,MI    "Y .dP'    `YM. MMN.    M  
# dM'       `   MM   ,M9   `MM.M'        mmMMmm ,pW"Wq.           MM `MMb.     dM'      `MM M YMb   M  
# MM            MMmmdM9      MMb           MM  6W'   `Wb          MM   `YMMNq. MM        MM M  `MN. M  
# MM.    `7MMF' MM         ,M'`Mb.         MM  8M     M8          MM .     `MM MM.      ,MP M   `MM.M  
# `Mb.     MM   MM        ,P   `MM.        MM  YA.   ,A9     (O)  MM Mb     dM `Mb.    ,dP' M     YMM  
#   `"bmmmdPY .JMML.    .MM:.  .:MMa.      `Mbmo`Ybmd9'       Ymmm9  P"Ybmmd"    `"bmmd"' .JML.    YM  

# Autori:
#   Valentino Bocchetti & Mario Carofano

from gpx_converter import Converter # Convertitore GPX -> JSON (https://github.com/nidhaloff/gpx-converter)
import datetime as dt # Utilizziamo la data per gestire il nome del file prodotto dalla conversione
import json # Formattazione del file post conversione: TODO


def Conversion():
    output = dt.datetime.now().strftime('%d-%m-%y-%H:%M') + '.json'
    Converter(input_file='file.gpx').gpx_to_json(output_file=output)

print('Inizio Conversione')
Conversion()
print('DONE!')
