# Lista del pool di utenti presenti su cognito
# Richiede Access KEY e Secret KEY (Salviamo in 2 variabili, o da un file criptato leggendolo)

import boto3 # https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/cognito-idp.html#CognitoIdentityProvider.Client.list_users
import re
import json
import datetime

# JSON non serializable -> Da risolvere -> Creazione struttura dati che ospiti la response
def defaultconvert(o):
    if isinstance(o, datetime.datetime):
        return o.__str__()

def lamda_handler(event, context):
    access_key = 'DA PRENDERE'
    secret_key = 'DA PRENDERE'
    region = 'us-east-1'
    client = boto3.client('cognito-idp', ** {
        'aws_access_key_id': access_key, # https://www.msp360.com/resources/blog/how-to-find-your-aws-access-key-id-and-secret-access-key/
        'aws_secret_access_key': secret_key,
        'region_name': region
    })
    response = client.list_users(
        UserPoolId = 'DA PRENDERE DA COGNITO',
        AttributesToGet = [
            'email'
        ])

    listUsers = (json.dump(response, default = defaultconverter))
    return(re.findall(r"\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Z|a-z]{2,}\b", listUsers)) # Get dell'email (crearne una anche per gli username da utilizzare in fase di preregistrazione)
    return listUsers
