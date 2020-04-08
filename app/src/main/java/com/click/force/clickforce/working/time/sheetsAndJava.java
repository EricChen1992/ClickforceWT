package com.click.force.clickforce.working.time;

public class sheetsAndJava {
////    private static Sheets sheetsService;
//    private static String APPLICIATION_NAME = "Google Sheets Example";
//    public static String SPREADSHEET_ID = "10-Xbexfkkzvlxpg-g0AFCRZkFcHoDA1er6Fg1E1hJjI";

//    private static Credential authorize() throws IOException, GeneralSecurityException{
//        InputStream in = sheetsAndJava.class.getResourceAsStream("/credentials.json");
//        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
//            JacksonFactory.getDefaultInstance(),
//            new InputStreamReader(in)
//        );
//
//        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
//
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
//                clientSecrets, scopes)
//                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
//                .setAccessType("offline")
//                .build();
//        Credential credential = new AuthorizationCodeInstalledApp(
//                flow, new LocalServerReceiver())
//                .authorize("user");
//
//        return credential;
//    }
//
//    public static  Sheets getSheetsService() throws IOException, GeneralSecurityException{
//        Credential credential = authorize();
//        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
//                JacksonFactory.getDefaultInstance(), credential)
//                .setApplicationName(APPLICIATION_NAME)
//                .build();
//    }
//
//    public static void main(String[] args) throws IOException, GeneralSecurityException{
//        sheetsService = getSheetsService();
//        String range = "congress!A2:D2";
//        ValueRange response = sheetsService.spreadsheets().values()
//                .get(sheetsAndJava.SPREADSHEET_ID, range)
//                .execute();
//        List<List<Object>> values = response.getValues();
//        if (values == null || values.isEmpty()){
//            Log.e("Test","No data found.");
//        } else {
//            for (List row : values){
//                Log.e("Test", row.get(5)+" "+row.get(4) + " from " +row.get(1)+ " \n");
//
//            }
//        }
//    }
}
