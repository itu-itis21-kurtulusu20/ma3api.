using System;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore
{
    class TestData
    {
        public static readonly String LevelBCAOK =
            //*
            "MIIFTTCCBDWgAwIBAgICCxAwDQYJKoZIhvcNAQEFBQAwfDETMBEGCgmSJomT8ixkARkWA05FVDES" +
                    "MBAGCgmSJomT8ixkARkWAlVHMRIwEAYDVQQKDAlUw5xCxLBUQUsxDjAMBgNVBAsMBVVFS0FFMS0w" +
                    "KwYDVQQDDCTDnHLDvG4gR2VsacWfdGlybWUgU2VydGlmaWthIE1ha2FtxLEwHhcNMTIwNzI0MTMx" +
                    "MjI3WhcNMTQwNzI0MTMxMjI3WjAZMRcwFQYDVQQDDA5Tw7xsZXltYW4gVXNsdTCCASIwDQYJKoZI" +
                    "hvcNAQEBBQADggEPADCCAQoCggEBAI9MXkBxlOX/DpzMrPopFZv4fj2h02p0YkIOr9nREXAT/aLW" +
                    "eECdPxXqTK5zBolLc3WLo3vsSbJM25Q3uAm+gmHpKPNYo7lbM4ZuQ1+vz2cud0ZCnTUQeHIYbnZ/" +
                    "PiqUaan9qKUROkPMfHALCRUETmF5X+mkAEdcKzIotJht4eLR7fb9954uO7FM1eOUp/bNXDlRKQA3" +
                    "MbwVT+fZdRzYhpSn12lvVHO19ClvspeCCh1hhrOCTRSME5dpqD2C6EkoPQQl+4bIqP6h2VuGPEXs" +
                    "rdAb6CSLkcmjy3cQXrBV8I9rvyCVD0BOQHe17rXqGOGX0uYWW2ckRNbA4dYYN35gECkCAwEAAaOC" +
                    "AjowggI2MB8GA1UdIwQYMBaAFPzoTs2ckQcsoUHZPGFyf5UQL/erMB0GA1UdDgQWBBRohkjm0bLT" +
                    "khvoHs/AxJaX9RhMHDAOBgNVHQ8BAf8EBAMCBSAwCQYDVR0TBAIwADCBswYDVR0fBIGrMIGoMCKg" +
                    "IKAehhxodHRwOi8vZGVwby51Zy5uZXQvVUdTSUwuY3JsMIGBoH+gfYZ7bGRhcDovL3VnLm5ldC9D" +
                    "Tj1VRyBTTSxDTj1VRyxPVT1VRUtBRSxPPVTcQjBUQUssREM9VUcsREM9TkVUP2NlcnRmaWNhdGVS" +
                    "ZXZvY2F0aW9uTGlzdD9iYXNlP29iamVjdGNsYXNzPWNSTERpc3RyaWJ1dGlvblBvaW50MIHbBggr" +
                    "BgEFBQcBAQSBzjCByzAoBggrBgEFBQcwAoYcaHR0cDovL2RlcG8udWcubmV0L1VHS09LLmNydDB+" +
                    "BggrBgEFBQcwAoZybGRhcDovL3VnLm5ldC9DTj1VRyBTTSxDTj1VRyxPVT1VRUtBRSxPPVTcQjBU" +
                    "QUssREM9VUcsREM9TkVUP2NBQ2VydGlmaWNhdGU/YmFzZT9vYmplY3RjbGFzcz1jZXJ0aWZpY2F0" +
                    "aW9uQXV0aG9yaXR5MB8GCCsGAQUFBzABhhNodHRwOi8vb2NzcC51Zy5uZXQvMEUGA1UdEQQ+MDyB" +
                    "FHN1bGV5bWFuLnVzbHVAdWcubmV0oCQGCisGAQQBgjcUAgOgFgwUc3VsZXltYW4udXNsdUB1Zy5u" +
                    "ZXQwDQYJKoZIhvcNAQEFBQADggEBAE8jQQs6BTR7X6YdCy5hQgWovDCfrrdFVYajioILcOGyMIsH" +
                    "Zqnz/JKAvO5JWVMXKR/onnJP/fMKkUOPs11OLOWPW0Sotnr0AVHKe6Eq1N8TnXeKNnoAkiPOlwfb" +
                    "HtOHf7q6a6EBetw6U9Isw+eVftOr6fTl8FvKE8jsJej2om+LEWhS49bDk8Tfu96JW4HZw1bcWZON" +
                    "Db0EhIdQ/Wnt8Wk3JApeOQnw7mMbqThDtTuBJob6fRT9RBHIqcWY/gZhhq/iDswMeHYHZ6ASUj88" +
                    "5ryXaBL7s7UkgeIrgcs8CFROht8okoEjt8M52jwKvT/GRnT0pmbylj/2E/O0pzW1hL0=";//*/
            /*"MIIEnTCCA4WgAwIBAgIHA7KCK6xCzjANBgkqhkiG9w0BAQUFADBXMQswCQYDVQQG" +
                    "EwJGUjENMAsGA1UEChMERVRTSTEkMCIGA1UECxMbUGx1Z3Rlc3RzIFNURi0zNTEg" +
                    "MjAwOC0yMDA5MRMwEQYDVQQDEwpMZXZlbEFDQU9LMB4XDTA4MDgyMjE1MDM0MVoX" +
                    "DTExMDgyMTE0NTg0OFowVzELMAkGA1UEBhMCRlIxDTALBgNVBAoTBEVUU0kxJDAi" +
                    "BgNVBAsTG1BsdWd0ZXN0cyBTVEYtMzUxIDIwMDgtMjAwOTETMBEGA1UEAxMKTGV2" +
                    "ZWxCQ0FPSzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAIMhshucEmWS" +
                    "1SHqMfYVweyftu3DYjuF89zC7EskODxdcexnsJwGeEEEHcJAJdChVhAkAhKJXPgX" +
                    "Co2w3R6jOe+fHP5iy/XGh1lXUL5v+dPbiZeQmdxAsQ7PMr5ShKYGCEz+hHGyrqoi" +
                    "15vXZrFic1VsAU498sOEH8w6WjMiOtIe2ZUtf3yXYj4O+GSVTNNVY6MPt5O0yyV1" +
                    "/J1QJknkDkRr2FHRMrmAEBvhmHobk0czO4FSIXlhL2wwkp/tNiagleQVoVaQGVgV" +
                    "JnYQmOHyrmmALKzq9ShwT+i5ODzaa1Ixq1k0MNK3nkJ1TZbdPMVGB4qArjppveUy" +
                    "zgJIk/xhFk0CAwEAAaOCAWwwggFoMA4GA1UdDwEB/wQEAwIBxjAPBgNVHRMBAf8E" +
                    "BTADAQH/MB0GA1UdDgQWBBTo78xzkPrkyl29V+dkisYw60YoCjBWBgNVHR8ETzBN" +
                    "MEugSaBHhkVodHRwOi8veGFkZXMtcG9ydGFsLmV0c2kub3JnL3Byb3RlY3RlZC9j" +
                    "YXBzby9jcmxzL1NDT0svTGV2ZWxBQ0FPSy5jcmwwgawGCCsGAQUFBwEBBIGfMIGc" +
                    "MEsGCCsGAQUFBzABhj9odHRwOi8veGFkZXMtcG9ydGFsLmV0c2kub3JnL3Byb3Rl" +
                    "Y3RlZC9jYXBzby9PQ1NQP2NhPUxldmVsQUNBT0swTQYIKwYBBQUHMAKGQWh0dHA6" +
                    "Ly94YWRlcy1wb3J0YWwuZXRzaS5vcmcvcHJvdGVjdGVkL2NhcHNvL2NlcnRzL0xl" +
                    "dmVsQUNBT0suY3J0MB8GA1UdIwQYMBaAFFJaeLMGgH2vNOFkuoVLnRW6G7w2MA0G" +
                    "CSqGSIb3DQEBBQUAA4IBAQAtics932iPciPhIFdumB07LJusySgaWfiu6uB1CITR" +
                    "WqwnJvfIBaYfgKUgcf+PaBywqYIdyojBGs8YNZCKwdivC9GOXt52DyxZQlB+U0yu" +
                    "marJL04CMrXvKkqkKyxuRseXp9cMwYhGMDvUqWn2Jqg+n13uxUOEh68eHLntG3vy" +
                    "1B9JScNGGrMljOd/8d4Ev8bjVpp9zKYkK/Fz8SDYMowlyYu70v4Z2M63RxnjdlpH" +
                    "SrkQn0Ay5rqmqt9KxYrYVKv8io1ND/I688vUADULN+0lCOhowohRphcybDfk0ep/" +
                    "eoWmYwK7iZ5/fTUFEfyPEV1YQo5ILb0uk+mFV5oKu6u/";//*/
        public static readonly String KOKSIL =
                "MIICqTCCAZECAQEwDQYJKoZIhvcNAQEFBQAwggErMQswCQYDVQQGEwJUUjEYMBYGA1UEBwwPR2Vi" +
                        "emUgLSBLb2NhZWxpMUcwRQYDVQQKDD5Uw7xya2l5ZSBCaWxpbXNlbCB2ZSBUZWtub2xvamlrIEFy" +
                        "YcWfdMSxcm1hIEt1cnVtdSAtIFTDnELEsFRBSzFIMEYGA1UECww/VWx1c2FsIEVsZWt0cm9uaWsg" +
                        "dmUgS3JpcHRvbG9qaSBBcmHFn3TEsXJtYSBFbnN0aXTDvHPDvCAtIFVFS0FFMSMwIQYDVQQLDBpL" +
                        "YW11IFNlcnRpZmlrYXN5b24gTWVya2V6aTFKMEgGA1UEAwxBVMOcQsSwVEFLIFVFS0FFIEvDtmsg" +
                        "U2VydGlmaWthIEhpem1ldCBTYcSfbGF5xLFjxLFzxLEgLSBTw7xyw7xtIDMXDTA5MTEwNDEyNTAx" +
                        "NVoXDTEwMDIwMzEyNTExNFqgMDAuMAsGA1UdFAQEAgIAjjAfBgNVHSMEGDAWgBS9iIfJj/akCguq" +
                        "68X+kSOdq0qKMjANBgkqhkiG9w0BAQUFAAOCAQEAM9DXoMmppFopXo0t5YgF624tA/HA334O+bHF" +
                        "WB2P4/upUA3uz2ThcYz1hyx7do++9jqbtB7/djuHL65zUpvDgJsqMCYJUQ2EOiF8fSDer9e1i9OH" +
                        "xxSkPY9+qF60p6Pp3lqR8WogO28DpMwDvByekpe5wy3o/ZOwo0osBMy6CcF2MltE6fly7MahBjT3" +
                        "ltxDe6T8A+VCDIR6r28mExSUSVTIvXXUv6l0sdzp+Ks6E1n8GCEgi8RaQr/LWn/aRac8Egbtczzy" +
                        "EUJ3EjhbdtjloI3Pjqzw82XS+JIhe4RZoxvP5wF9HDPYclfzY5tmhtJftyLR6hCouGbsSzroE2J2" +
                        "6w==";

        //LevelBCAOK'a ait ocsp cevabi
        public static readonly String LevelBCAOK_BOR =
            //*
            "MIIGzgoBAKCCBscwggbDBgkrBgEFBQcwAQEEgga0MIIGsDCBpqIWBBS28Ks+mrIZqd5mBK0Z6sd6" +
                    "eEwteBgSMjAxMjEyMjAxMjQ5NTQuOThaMFMwUTA5MAcGBSsOAwIaBBQgOPHzdI6X725cBu+mjxRT" +
                    "JjQ0/wQU/OhOzZyRByyhQdk8YXJ/lRAv96sCAgsQgAAYEjIwMTIxMjIwMTI0OTU0Ljk4WqEjMCEw" +
                    "HwYJKwYBBQUHMAECBBIEEC21QobxOJTCNxV44D9wyTowDQYJKoZIhvcNAQEFBQADggEBAKe0MHjV" +
                    "Tx8fzhUc6Fp6Tf4o8DPINDcqdTl6SrZ90U9BcljywRGHuCibAe1pSw/6jXy58ZUOdLlp6jPkQlXM" +
                    "9ra4ye5xPDTt3TebcRkGO4Vt65TZdYlaHvC3K35jn9/gama8ITRpYJmYegjhUvw9EgOI+8oB7JbG" +
                    "ltrSGkvv1PtB6ZBQaBXbjRHt+OmTzk2MFqc02tI5KrBmRviVKZIG7X+LQfQhxSWes2oGY4mkbp6Z" +
                    "0T9ZPOHQ/rD+lxRahuDAugl3Bx/DrIXI0B81mn5uWxwr4qpzFW3+t3d9ffA0BvfOFxUi+ojg83JJ" +
                    "g6cssMpo+6fgjwYnsUXjl2b5mF450iegggTvMIIE6zCCBOcwggPPoAMCAQICARUwDQYJKoZIhvcN" +
                    "AQEFBQAwfDETMBEGCgmSJomT8ixkARkWA05FVDESMBAGCgmSJomT8ixkARkWAlVHMRIwEAYDVQQK" +
                    "DAlUw5xCxLBUQUsxDjAMBgNVBAsMBVVFS0FFMS0wKwYDVQQDDCTDnHLDvG4gR2VsacWfdGlybWUg" +
                    "U2VydGlmaWthIE1ha2FtxLEwHhcNMTAwMzEwMDg0NDAwWhcNMTMwMzA5MDg0NDAwWjASMRAwDgYD" +
                    "VQQDDAdVRyBPQ1NQMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzxjlm+by5x4DsUhT" +
                    "wlaQRSlmmm8k0f+s7zFFCPCJKFesQ2qwBfjYxruPCK7827aLNQFEU+kdYxt8lABcxzTh/VEsR5e9" +
                    "teNNr2b3llMVxYHc0HkLl7Ymde7p1+HEUM3dE5SQKRYxO3wUUE487jNnZmWamEArW2q3dSaREEk4" +
                    "hnLd9LSI3sjkZnMHs5BAhTHZXeGwSH87aVm8OOE8yd9P40n1b5ojmfinYSE2Hkeb60cHDMB4L0d+" +
                    "+7Z1CImWieT3sIU4AfIQmcI0RBQmh6juEEs6kpe676M3bNQr8Qr0LVUbDb5tsG5nyp2XC91zwWGB" +
                    "8cTpVqyePS3wsXZ9JU5jIQIDAQABo4IB3DCCAdgwHwYDVR0jBBgwFoAU/OhOzZyRByyhQdk8YXJ/" +
                    "lRAv96swHQYDVR0OBBYEFLbwqz6ashmp3mYErRnqx3p4TC14MA4GA1UdDwEB/wQEAwIGwDATBgNV" +
                    "HSUEDDAKBggrBgEFBQcDCTCBswYDVR0fBIGrMIGoMCKgIKAehhxodHRwOi8vZGVwby51Zy5uZXQv" +
                    "VUdTSUwuY3JsMIGBoH+gfYZ7bGRhcDovL3VnLm5ldC9DTj1VRyBTTSxDTj1VRyxPVT1VRUtBRSxP" +
                    "PVTcQjBUQUssREM9VUcsREM9TkVUP2NlcnRmaWNhdGVSZXZvY2F0aW9uTGlzdD9iYXNlP29iamVj" +
                    "dGNsYXNzPWNSTERpc3RyaWJ1dGlvblBvaW50MIG6BggrBgEFBQcBAQSBrTCBqjAoBggrBgEFBQcw" +
                    "AoYcaHR0cDovL2RlcG8udWcubmV0L1VHU0lMLmNlcjB+BggrBgEFBQcwAoZybGRhcDovL3VnLm5l" +
                    "dC9DTj1VRyBTTSxDTj1VRyxPVT1VRUtBRSxPPVTcQjBUQUssREM9VUcsREM9TkVUP2NBQ2VydGlm" +
                    "aWNhdGU/YmFzZT9vYmplY3RjbGFzcz1jZXJ0aWZpY2F0aW9uQXV0aG9yaXR5MA0GCSqGSIb3DQEB" +
                    "BQUAA4IBAQC3Fa8zxgSPpm7R1xki5CvxPbxbSkDbUASgBIoUj7y1Q1rAFZEFBOs8roBSs6LBCuE3" +
                    "kgklBqn5R6jgUBSXlmMWczUIoV38G7VS5Ojc0zPRgms9lCuCQGt+BEXBwnAdZ3rDdt2wwpy7EuTZ" +
                    "qohywB3rg/zVqNBcRmk/wyNUKNt6DP2YrQDIh/GACaAHvnBvnOD6OKnbWxHXea8TFrd8kg+SECRL" +
                    "cyoBqvHQHe2g+y8Pi+nQAfoj4MkcrpmZ57dss4+lOE+7R5PUnibO/Lqv1RTsS7ALAaC9ZMyqThMN" +
                    "jQagghu0QlojQW4OeTsXEzLR+hR4IZzXtnhEjC5NmNzFTHdM";//*/
                /*"MIIHFTCCAVyhWTBXMQswCQYDVQQGEwJGUjENMAsGA1UEChMERVRTSTEkMCIGA1UECxMbUGx1Z3Rl" +
                        "c3RzIFNURi0zNTEgMjAwOC0yMDA5MRMwEQYDVQQDEwpMZXZlbEFDQU9LGA8yMDEwMTAzMTEyMjU0" +
                        "MFowVzBVMEAwCQYFKw4DAhoFAAQUpDiCpNojYoIRSD2sQRr9uryFQbMEFFJaeLMGgH2vNOFkuoVL" +
                        "nRW6G7w2AgcDsoIrrELOgAAYDzIwMTAxMDMxMTIyNTQwWqGBlDCBkTCBjgYJKwYBBQUHMAECBIGA" +
                        "eBf6GyIbIr299Otbt2Dtt/97utl2NtvUetub9M1HnP0TFbVoF0UY7HdN3s5Z5800UuSZbgb2sQRu" +
                        "HtA2K2Bz5o8nOIgV/Jl4Zkwq/0f6bB7a1JxVo+6IgRds7izytGGXUBOvvEB+RUzyDLO5Klp1227o" +
                        "mFi7XVkvk9Qk5LfuKbMwDQYJKoZIhvcNAQEFBQADggEBACnp/nUImq9PU49oUZ+mlBDYCEgPtx0h" +
                        "/ca6oIDggyBgaFM3A1+fZoz/mbIBEZml8/C7v/ftKog2E6ECuo0vjWBCFuELoBNzvx5lk+gPDOKG" +
                        "ynapyYNBV4lL3MFBNfHG+AoMp7Wh4RiZco5zq3aandVZrNrXbNCYCv68arEGt3JhJ34R8iau3PSc" +
                        "Ixp+Mu9+7XPuy9MEn46OSCaNSbRZLzqvpih60erY+ecAvNgfX3SdeYpUQLpQc3cbMwH7J7zy6Jwf" +
                        "whLuTphFj4dDgewTtLyiU0iRcKkyixl42D7X8O/Bh2b5dwaH3Zn6HswmIPZa4n2lMiyPbBmwn1/Y" +
                        "xz8ytHygggSdMIIEmTCCBJUwggN9oAMCAQICBwL0K2EARCYwDQYJKoZIhvcNAQEFBQAwVTELMAkG" +
                        "A1UEBhMCRlIxDTALBgNVBAoTBEVUU0kxJDAiBgNVBAsTG1BsdWd0ZXN0cyBTVEYtMzUxIDIwMDgt" +
                        "MjAwOTERMA8GA1UEAxMIUm9vdENBT0swHhcNMDgwODIyMTQ1ODQ4WhcNMTEwODIyMTQ1ODQ4WjBX" +
                        "MQswCQYDVQQGEwJGUjENMAsGA1UEChMERVRTSTEkMCIGA1UECxMbUGx1Z3Rlc3RzIFNURi0zNTEg" +
                        "MjAwOC0yMDA5MRMwEQYDVQQDEwpMZXZlbEFDQU9LMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIB" +
                        "CgKCAQEAkqIaGfcp7TtpQGU6ge4wXfJFRaXQaa84T/rRZLRyeJTwe5wlM0k1WIOieiK/Ck71g9xY" +
                        "oCLNJR/tNK3dphP2HjfwtWTQZE1JX0C2QqIQXYk1+fylZxptbp2WkD94iGjuOemW5y5dNBTFSGFz" +
                        "gPKKabyIUP7YnPLJxw3sgiwU5ipqaja+Em88Zv6pUezHI2Lvr6KeUOwWvOrcpqh4mvpGwb0L+XdK" +
                        "wB/CxGzIeFVHtgSsEBpp+f8HgIqArU8O8lQB8l830REYJN8Upu27X5PlWlAuLIaGeQuakWMVvjeQ" +
                        "XPNq2sVzjhKYwZdRx4QKNCawIG1xFcpWfYxNFvAUBYqizwIDAQABo4IBZjCCAWIwDgYDVR0PAQH/" +
                        "BAQDAgHGMA8GA1UdEwEB/wQFMAMBAf8wHQYDVR0OBBYEFFJaeLMGgH2vNOFkuoVLnRW6G7w2MFQG" +
                        "A1UdHwRNMEswSaBHoEWGQ2h0dHA6Ly94YWRlcy1wb3J0YWwuZXRzaS5vcmcvcHJvdGVjdGVkL2Nh" +
                        "cHNvL2NybHMvU0NPSy9Sb290Q0FPSy5jcmwwgagGCCsGAQUFBwEBBIGbMIGYMEkGCCsGAQUFBzAB" +
                        "hj1odHRwOi8veGFkZXMtcG9ydGFsLmV0c2kub3JnL3Byb3RlY3RlZC9jYXBzby9PQ1NQP2NhPVJv" +
                        "b3RDQU9LMEsGCCsGAQUFBzAChj9odHRwOi8veGFkZXMtcG9ydGFsLmV0c2kub3JnL3Byb3RlY3Rl" +
                        "ZC9jYXBzby9jZXJ0cy9Sb290Q0FPSy5jcnQwHwYDVR0jBBgwFoAUyBqr9vAhEsI+B/unvEQ+yy8t" +
                        "OCAwDQYJKoZIhvcNAQEFBQADggEBAFxbQTS8LglZFZO2SP+rsw4qRtQvPOvg/Rl4xdAJI8bNRu5g" +
                        "qo/FrZ6dKCQjqtljyJXmHVO6UwJg5RegdQeiozRvTEh91KHklQDnZhbgO37OEtOrtg/72u1R00Bg" +
                        "5Z1coBvCKsbVR+f85vwMHbIxOV8EPSHsdml/rborIAUF7jNTPcuiDBI9aTYdUyhTo/QgLZSaBiOj" +
                        "TAPPu/BOp6mqZPti3UbgAOLlxmnaDOxkIShgRc7lc/rsPGa77Oh2zf7CR235NWGFLkJFx6Iojxtd" +
                        "G1IV3fvlGUtH2oceXJIL7L66Y6NSLnbmzO+Kul2/rG7V0VBPDlgKuFYTdxlz9Jsmrhc=";*/

        public static readonly String USER_UG_SIGN =          
            "MIIE+DCCA+CgAwIBAgICCbMwDQYJKoZIhvcNAQEFBQAwfDETMBEGCgmSJomT8ixkARkWA05FVDES" +
                    "MBAGCgmSJomT8ixkARkWAlVHMRIwEAYDVQQKDAlUw5xCxLBUQUsxDjAMBgNVBAsMBVVFS0FFMS0w" +
                    "KwYDVQQDDCTDnHLDvG4gR2VsacWfdGlybWUgU2VydGlmaWthIE1ha2FtxLEwHhcNMTEwNDA3MTI1" +
                    "MTEwWhcNMTMwNDA2MTI1MTEwWjAbMRkwFwYDVQQDDBBCaWxlbiDDlsSfcmV0bWVuMIGfMA0GCSqG" +
                    "SIb3DQEBAQUAA4GNADCBiQKBgQDbY+hAr4nP8tmfbW+5p/H4x9ZqKrTSWTRkdBpl3pJVgp/uuNCN" +
                    "tbdPx+ZjnSA2VbWjDlLyvRlqUxySlPxLfV2fxKe9AfMC3vt0Jk2zWmb7tdt4KsIPLybElcpkUu0l" +
                    "2c1xYsogs26Z1SXtI5d6UCiiyNinPZhknASr7nVIWHjL0QIDAQABo4ICZzCCAmMwHwYDVR0jBBgw" +
                    "FoAU/OhOzZyRByyhQdk8YXJ/lRAv96swHQYDVR0OBBYEFKIeCKqDQiXamVl2JyXW4D2IPPylMA4G" +
                    "A1UdDwEB/wQEAwIGwDAJBgNVHRMEAjAAMCkGA1UdJQQiMCAGCCsGAQUFBwMCBggrBgEFBQcDBAYK" +
                    "KwYBBAGCNxQCAjCBswYDVR0fBIGrMIGoMCKgIKAehhxodHRwOi8vZGVwby51Zy5uZXQvVUdTSUwu" +
                    "Y3JsMIGBoH+gfYZ7bGRhcDovL3VnLm5ldC9DTj1VRyBTTSxDTj1VRyxPVT1VRUtBRSxPPVTcQjBU" +
                    "QUssREM9VUcsREM9TkVUP2NlcnRmaWNhdGVSZXZvY2F0aW9uTGlzdD9iYXNlP29iamVjdGNsYXNz" +
                    "PWNSTERpc3RyaWJ1dGlvblBvaW50MIHbBggrBgEFBQcBAQSBzjCByzAoBggrBgEFBQcwAoYcaHR0" +
                    "cDovL2RlcG8udWcubmV0L1VHS09LLmNydDB+BggrBgEFBQcwAoZybGRhcDovL3VnLm5ldC9DTj1V" +
                    "RyBTTSxDTj1VRyxPVT1VRUtBRSxPPVTcQjBUQUssREM9VUcsREM9TkVUP2NBQ2VydGlmaWNhdGU/" +
                    "YmFzZT9vYmplY3RjbGFzcz1jZXJ0aWZpY2F0aW9uQXV0aG9yaXR5MB8GCCsGAQUFBzABhhNodHRw" +
                    "Oi8vb2NzcC51Zy5uZXQvMEcGA1UdEQRAMD6BFWJpbGVuLm9ncmV0bWVuQHVnLm5ldKAlBgorBgEE" +
                    "AYI3FAIDoBcMFWJpbGVuLm9ncmV0bWVuQHVnLm5ldDANBgkqhkiG9w0BAQUFAAOCAQEAMbtlm2KC" +
                    "dGgVMnph5v8tZfET5ruO4XtzJqLbbhN4juy8aadHCmifwW3dqgXEtA5y+BfJMg8IHWakniaxUZ4m" +
                    "FfHkjIyMYKGVYyYamPWdxFeNOsXnPz02Hr8LXflo4ZTyOjPHDKtWnUZ+l7xxTgu7QcunML5o6F3F" +
                    "u3yeNO+TU6OJL22WovfRvC6dtZQJN2bPfBfQOiWWiTY6Mv60IkD/xpj2otIe+2j6DYMdIeAA8jRK" +
                    "IaBWD2LBLqToMu6JolesMFEILsGQ5EuiTXFc9qYIXhYp2v1SabsvjEERiKexi2Kam++4HIXvz6S9" +
                    "YFgTUawgjqZxB5n9cS2Lda12qQ0tyg==";

        public static readonly String UG_KOK =
                "MIIDtTCCAp2gAwIBAgIBATANBgkqhkiG9w0BAQUFADB8MRMwEQYKCZImiZPyLGQB" +
                        "GRYDTkVUMRIwEAYKCZImiZPyLGQBGRYCVUcxEjAQBgNVBAoMCVTDnELEsFRBSzEO" +
                        "MAwGA1UECwwFVUVLQUUxLTArBgNVBAMMJMOccsO8biBHZWxpxZ90aXJtZSBTZXJ0" +
                        "aWZpa2EgTWFrYW3EsTAeFw0xMDAzMDkxMzQxMThaFw0yMDAzMDkxMzQxMThaMHwx" +
                        "EzARBgoJkiaJk/IsZAEZFgNORVQxEjAQBgoJkiaJk/IsZAEZFgJVRzESMBAGA1UE" +
                        "CgwJVMOcQsSwVEFLMQ4wDAYDVQQLDAVVRUtBRTEtMCsGA1UEAwwkw5xyw7xuIEdl" +
                        "bGnFn3Rpcm1lIFNlcnRpZmlrYSBNYWthbcSxMIIBIjANBgkqhkiG9w0BAQEFAAOC" +
                        "AQ8AMIIBCgKCAQEAvVwh2cLvJZvVuF2JA1G1n4W4lZkdZKYF58C5WiRSZX+GvTub" +
                        "Hn6OHtagjXZ35JzP3BBJ17qpInUCzdeOSMCLKNMMudIIsp/I4QO7DbT0IAA6gjyD" +
                        "S1Ore0dW7KtQtPRgJrbmWeCEfFl2QdMPM7ybKqNfpjnruSOz5tmQHDM/lH4D2hoH" +
                        "sbAYD0fdxirVM983dN9VvQWId7+CSiCtZNBUkAiDvLDUAZH3zCPf+Epx0hyMYaVi" +
                        "an4Cd4lLD1Jis4FeCcV3AQ3RAT6cUlNDXpOFWla2Mln3lV2oiGclzPc/qxWAMGCi" +
                        "xBdnACw4hlhKpyuACf0WD4BPlA2TThjMd4bDgQIDAQABo0IwQDAdBgNVHQ4EFgQU" +
                        "/OhOzZyRByyhQdk8YXJ/lRAv96swDgYDVR0PAQH/BAQDAgEGMA8GA1UdEwEB/wQF" +
                        "MAMBAf8wDQYJKoZIhvcNAQEFBQADggEBAKU+nkeRiNaMZgnexE+btXVy51Xj6K0A" +
                        "4G/+BrO/9bq0toip69AXnowdslC5odnJuiwjoyg0CAixORk6S04xtF/SYviTPhiy" +
                        "DaybPe7pPOb11hd7AeKu7S9oSbWsuINSV9lwhqCxUviqyS5zrm0y41aUUkprcA3j" +
                        "KlVPTuUUxOK253JQZgY0wtOQj0NffmTSVkBPPHjVILjzvGy4c5+lV+5XpCPcj32m" +
                        "Nza1eiWnUVp8pHtlFPGq1CjPmdnVfa5PEzAqASuGN2PzeY1bJxikepuTZKRpL+qm" +
                        "dNR1mSIMCTPruleRN2cAFP0yC1bZBgCkBh4MUzGgE7hkq3iIADAT/pg=";

    }
}
