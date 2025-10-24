package com.jamuara.crs.activities.service;
import com.jamuara.crs.activities.dto.ActivityResponse;

import java.util.*;

public class SampleActivityData {

    public static Map<String, List<ActivityResponse>> getCityAttractions() {
        Map<String, List<ActivityResponse>> attractionsMap = new HashMap<>();

        // New Delhi (28.6139, 77.2090)
        attractionsMap.put("28.6139,77.209", Arrays.asList(
                new ActivityResponse("139368610", "activity", "Audio Guided Walk of Humayun's Tomb & Nizammudin Basti, Delhi on HopOn India App",
                        "null", "s, from where we lead you through the narrow, clamorous streets of Nizamuddin Basti another, very different tomb – the dargah of the famous Sufi saint Hazrat Nizamuddin Aulia. This is a very unique experience of the Humayun tomb &amp; Nizamuddin Basti.</p><p>The most special features of this experience are : </p><p>1. The App opens up new places and new stories for you in the same old cities. </p><p>2. Each walk is crafted like a masterpiece to offer an immersive experience to the traveller with the correct mix of history, culture, myth, food , through professional narration, with background scores of music, qawalli or sound affects here and there. </p><p>3. There is no need for you to depend on a guide - the traveller can take the walk anytime as per will, at his/ her own pace. </p><p>4. The content is developed by domain experts. </p><p>5. You pay once for three months and need not pay the guide repeatedly</p></div>",
                        new ActivityResponse.GeoCode("28.6129", "77.2295"),
                        List.of( "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzL2Y5Y2QzZjk1LWQ5NjMtNDMxNC1hZjViLTA3N2IwODE2ZTU0NyIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzL2RiNGFkMjdlLTAxMDUtNDFmMC04MzJjLWI3MTlkMzRjNWZhNiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzdkM2Y3OWRmLTcxNTMtNGMxMS05NTcwLWI0N2Y0Mjk1ZjVlMyIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzE2Nzc1ZTI2LTU5ZTQtNDQzNC1iYWFkLTAwYTY1ZGJkMDdmOCIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzYwNDAzNDY0LWQyYzMtNDdlYS1hNjIzLWEzODMzZGM2NDA5MyIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0="
                        ),
                        "https://amadeus.booking.holibob.tech/product/4c5904a8-afe1-48af-b281-cc0746d54594",
                        new ActivityResponse.Price("INR", "467.0"), "", "4.6"),
                new ActivityResponse(
                        "139366603",
                        "activity",
                        "One Way Transfer From New Delhi To Agra Stress Free",
                        "null",
                        "<div><p>This is the one way drop option there are many option for one way drop</p><p>like </p><p>Agra to Delhi drop</p><p>Delhi to Agra drop</p><p>Delhi to Jaipur drop</p><p>Jaipur to Agra drop </p><p>Agra Airport to Agra Hotels</p></div>",
                        new ActivityResponse.GeoCode("28.6139391", "77.2090212"),
                        List.of(
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzc0MTI3M2UwLWI2ZGEtNDkyYy04YmZjLTA5NmIyMDVkM2Q2OCIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzL2Y5MjQ1OGM1LTJkNDEtNDEzZi04NWFmLTMzMWE1NGY0YzU5MyIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzZlZGQ1OTNjLTM4M2EtNDk2Ni1iODMwLTEzNmViMTRlMzYwMiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzRmMWM1ZTBmLTRkZTYtNGMzOC04ZDM4LTQ1ZmNmODk4NTgwYiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzL2Q2Y2M2ZTE5LTU1YWYtNDE5NS1iZGMyLWJmZDYwY2UwMDBhZCIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0="
                        ),
                        "https://amadeus.booking.holibob.tech/product/994e6d4b-7300-477c-80c1-3afebbcaf0d8",
                        new ActivityResponse.Price("INR", "612.0"),
                        "",
                        "null"
                ),
                new ActivityResponse(
                        "139369338",
                        "activity",
                        "Small-Group 5-Hour Food and Heritage Tour, Old Delhi",
                        "null",
                        "<div><p>Foodie travelers won’t want to miss this half-day food and heritage tour of Old Delhi. Avoid getting lost in the maze of streets or getting sick eating at the wrong places. On this small-group tour (max. 10) you’ll be taken to some of the best, cleanest, and most interesting street food spots that you might not find independently. Learn more about Old Delhi’s culture and history through its cuisine.</p></div>",
                        new ActivityResponse.GeoCode("28.6323437", "77.2177076"),
                        List.of(
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzZmMjEyMThiLWNkMTgtNDY0ZS05NzA4LTM5OWYxZDM5ZjM1NiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzU1NjZiYTYxLTM4MWUtNDEwZS1hMTU1LWNhYmQ1MmUzYjgxNCIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzhiYjMyMjViLWVjMDgtNDgzNS04N2FlLWZmNThlOWM4NWY1YSIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzL2E5M2Y5YWQ4LWVmNDktNDM4ZS04Yjk1LTIyNTNiNTAxZGVmMiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0="
                        ),
                        "https://amadeus.booking.holibob.tech/product/ce71177d-342c-4891-92f1-1cc53db44f65",
                        new ActivityResponse.Price("INR", "3851.0"),
                        "5 hours",
                        "null"
                ),
                new ActivityResponse(
                        "139368744",
                        "activity",
                        "Taj Mahal Day Tour from Delhi by Superfast Train",
                        "null",
                        "<div><p>Ditch Delhi's chaos and zoom to Agra in 1.5hrs on a comfy express train! Skip queues, delve deep with an expert guide. Immerse yourself in the Taj's mesmerizing beauty, a UNESCO wonder, as your guide unveils its fascinating history. Secrets revealed, memories captured in a professional photo – pure bliss!</p><p>More than just the Taj! This tour lets you explore Agra's rich tapestry. Discover the imposing Agra Fort, wander serene gardens, or savor delectable local cuisine.</p></div>",
                        new ActivityResponse.GeoCode("28.6139391", "77.2090212"),
                        List.of(
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzAyNDcyNTFlLTgzZjYtNDIyNS05Mjc2LTcyMDNlMWRjMzJjNSIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzk4MGQ3NjY4LWZjMjUtNDBjZS1iMmJkLWRmMGM4NTEyNGYxZCIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzJhMzVjZGY1LWVlMDMtNDZkZi1hZjMzLThjYTlkNzlmMWNlNSIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzZlMjZlNWZhLWFjMDUtNDZjYS04ODQwLTMyNWIwZTAxMzljZiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0="
                        ),
                        "https://amadeus.booking.holibob.tech/product/a17866e1-6483-4867-9897-6c7046628b3a",
                        new ActivityResponse.Price("INR", "3275.0"),
                        "",
                        "null"
                ),
                new ActivityResponse(
                        "139368691",
                        "activity",
                        "5 Days Luxury Golden Triangle Tour : Delhi Agra Jaipur Tour",
                        "null",
                        "<div><p>5 Days Luxury Golden Triangle Tour holiday package consist of a tour to major destinations of India - Delhi, Agra and Jaipur. The golden triangle tour is a quintessential introduction to the rich culture, tradition and history of India, especially for the first time travelers. It is a triangular combination of three Glorious and the most Charming cities: Delhi Agra Jaipur Tour. This is because of the Golden Triangle (Delhi-Agra-Jaipur) represents the Grandeur, Glory, and History of the true Indian Diverse Culture.</p></div>",
                        new ActivityResponse.GeoCode("28.6141527", "77.1959622"),
                        List.of(
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzJmZTZiYTBjLTk1MDktNGNiNC05ODhjLTEwNDAwZjM0MDdmZiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzA5OGI2ZjAxLWZkZDAtNDJjNS05MGMyLTEyODgyNWYxNjlmYSIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzljNDZiNGJjLWRhNTktNGIzZi1hODFiLWU4NTFiNjlkNjc4YiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzZiYjE1Yzg1LWVhMTEtNDVjZS04ZmE0LTNhNjg3MDcxNzMyZSIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzk0ZTdhYzFlLTAwMWItNDg0ZS05NjY3LWE3OWU0ZmE5YjJmNCIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0="
                        ),
                        "https://amadeus.booking.holibob.tech/product/fe4f6d8d-a82f-4f25-bd49-fdc3d7974088",
                        new ActivityResponse.Price("INR", "39391.0"),
                        "5 days",
                        "null"
                )
        ));

        attractionsMap.put("28.6139,77.2090", Arrays.asList(
                new ActivityResponse("139368610", "activity", "Audio Guided Walk of Humayun's Tomb & Nizammudin Basti, Delhi on HopOn India App",
                        "null", "s, from where we lead you through the narrow, clamorous streets of Nizamuddin Basti another, very different tomb – the dargah of the famous Sufi saint Hazrat Nizamuddin Aulia. This is a very unique experience of the Humayun tomb &amp; Nizamuddin Basti.</p><p>The most special features of this experience are : </p><p>1. The App opens up new places and new stories for you in the same old cities. </p><p>2. Each walk is crafted like a masterpiece to offer an immersive experience to the traveller with the correct mix of history, culture, myth, food , through professional narration, with background scores of music, qawalli or sound affects here and there. </p><p>3. There is no need for you to depend on a guide - the traveller can take the walk anytime as per will, at his/ her own pace. </p><p>4. The content is developed by domain experts. </p><p>5. You pay once for three months and need not pay the guide repeatedly</p></div>",
                        new ActivityResponse.GeoCode("28.6129", "77.2295"),
                        List.of( "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzL2Y5Y2QzZjk1LWQ5NjMtNDMxNC1hZjViLTA3N2IwODE2ZTU0NyIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzL2RiNGFkMjdlLTAxMDUtNDFmMC04MzJjLWI3MTlkMzRjNWZhNiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzdkM2Y3OWRmLTcxNTMtNGMxMS05NTcwLWI0N2Y0Mjk1ZjVlMyIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzE2Nzc1ZTI2LTU5ZTQtNDQzNC1iYWFkLTAwYTY1ZGJkMDdmOCIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzYwNDAzNDY0LWQyYzMtNDdlYS1hNjIzLWEzODMzZGM2NDA5MyIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0="
                                ),
                        "https://amadeus.booking.holibob.tech/product/4c5904a8-afe1-48af-b281-cc0746d54594",
                        new ActivityResponse.Price("INR", "467.0"), "", "4.6"),
                new ActivityResponse(
                        "139366603",
                        "activity",
                        "One Way Transfer From New Delhi To Agra Stress Free",
                        "null",
                        "<div><p>This is the one way drop option there are many option for one way drop</p><p>like </p><p>Agra to Delhi drop</p><p>Delhi to Agra drop</p><p>Delhi to Jaipur drop</p><p>Jaipur to Agra drop </p><p>Agra Airport to Agra Hotels</p></div>",
                        new ActivityResponse.GeoCode("28.6139391", "77.2090212"),
                        List.of(
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzc0MTI3M2UwLWI2ZGEtNDkyYy04YmZjLTA5NmIyMDVkM2Q2OCIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzL2Y5MjQ1OGM1LTJkNDEtNDEzZi04NWFmLTMzMWE1NGY0YzU5MyIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzZlZGQ1OTNjLTM4M2EtNDk2Ni1iODMwLTEzNmViMTRlMzYwMiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzRmMWM1ZTBmLTRkZTYtNGMzOC04ZDM4LTQ1ZmNmODk4NTgwYiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzL2Q2Y2M2ZTE5LTU1YWYtNDE5NS1iZGMyLWJmZDYwY2UwMDBhZCIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0="
                        ),
                        "https://amadeus.booking.holibob.tech/product/994e6d4b-7300-477c-80c1-3afebbcaf0d8",
                        new ActivityResponse.Price("INR", "612.0"),
                        "",
                        "null"
                ),
                new ActivityResponse(
                        "139369338",
                        "activity",
                        "Small-Group 5-Hour Food and Heritage Tour, Old Delhi",
                        "null",
                        "<div><p>Foodie travelers won’t want to miss this half-day food and heritage tour of Old Delhi. Avoid getting lost in the maze of streets or getting sick eating at the wrong places. On this small-group tour (max. 10) you’ll be taken to some of the best, cleanest, and most interesting street food spots that you might not find independently. Learn more about Old Delhi’s culture and history through its cuisine.</p></div>",
                        new ActivityResponse.GeoCode("28.6323437", "77.2177076"),
                        List.of(
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzZmMjEyMThiLWNkMTgtNDY0ZS05NzA4LTM5OWYxZDM5ZjM1NiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzU1NjZiYTYxLTM4MWUtNDEwZS1hMTU1LWNhYmQ1MmUzYjgxNCIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzhiYjMyMjViLWVjMDgtNDgzNS04N2FlLWZmNThlOWM4NWY1YSIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzL2E5M2Y5YWQ4LWVmNDktNDM4ZS04Yjk1LTIyNTNiNTAxZGVmMiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0="
                        ),
                        "https://amadeus.booking.holibob.tech/product/ce71177d-342c-4891-92f1-1cc53db44f65",
                        new ActivityResponse.Price("INR", "3851.0"),
                        "5 hours",
                        "null"
                ),
                new ActivityResponse(
                        "139368744",
                        "activity",
                        "Taj Mahal Day Tour from Delhi by Superfast Train",
                        "null",
                        "<div><p>Ditch Delhi's chaos and zoom to Agra in 1.5hrs on a comfy express train! Skip queues, delve deep with an expert guide. Immerse yourself in the Taj's mesmerizing beauty, a UNESCO wonder, as your guide unveils its fascinating history. Secrets revealed, memories captured in a professional photo – pure bliss!</p><p>More than just the Taj! This tour lets you explore Agra's rich tapestry. Discover the imposing Agra Fort, wander serene gardens, or savor delectable local cuisine.</p></div>",
                        new ActivityResponse.GeoCode("28.6139391", "77.2090212"),
                        List.of(
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzAyNDcyNTFlLTgzZjYtNDIyNS05Mjc2LTcyMDNlMWRjMzJjNSIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzk4MGQ3NjY4LWZjMjUtNDBjZS1iMmJkLWRmMGM4NTEyNGYxZCIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzJhMzVjZGY1LWVlMDMtNDZkZi1hZjMzLThjYTlkNzlmMWNlNSIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzZlMjZlNWZhLWFjMDUtNDZjYS04ODQwLTMyNWIwZTAxMzljZiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0="
                        ),
                        "https://amadeus.booking.holibob.tech/product/a17866e1-6483-4867-9897-6c7046628b3a",
                        new ActivityResponse.Price("INR", "3275.0"),
                        "",
                        "null"
                ),
                new ActivityResponse(
                        "139368691",
                        "activity",
                        "5 Days Luxury Golden Triangle Tour : Delhi Agra Jaipur Tour",
                        "null",
                        "<div><p>5 Days Luxury Golden Triangle Tour holiday package consist of a tour to major destinations of India - Delhi, Agra and Jaipur. The golden triangle tour is a quintessential introduction to the rich culture, tradition and history of India, especially for the first time travelers. It is a triangular combination of three Glorious and the most Charming cities: Delhi Agra Jaipur Tour. This is because of the Golden Triangle (Delhi-Agra-Jaipur) represents the Grandeur, Glory, and History of the true Indian Diverse Culture.</p></div>",
                        new ActivityResponse.GeoCode("28.6141527", "77.1959622"),
                        List.of(
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzJmZTZiYTBjLTk1MDktNGNiNC05ODhjLTEwNDAwZjM0MDdmZiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzA5OGI2ZjAxLWZkZDAtNDJjNS05MGMyLTEyODgyNWYxNjlmYSIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzljNDZiNGJjLWRhNTktNGIzZi1hODFiLWU4NTFiNjlkNjc4YiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzZiYjE1Yzg1LWVhMTEtNDVjZS04ZmE0LTNhNjg3MDcxNzMyZSIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzk0ZTdhYzFlLTAwMWItNDg0ZS05NjY3LWE3OWU0ZmE5YjJmNCIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0="
                        ),
                        "https://amadeus.booking.holibob.tech/product/fe4f6d8d-a82f-4f25-bd49-fdc3d7974088",
                        new ActivityResponse.Price("INR", "39391.0"),
                        "5 days",
                        "null"
                )
        ));

        // London (51.5074, -0.1278)
        attractionsMap.put("51.5074,-0.1278", Arrays.asList(
                new ActivityResponse(
                        "65709974",
                        "activity",
                        "Four Centuries of Entertainment on London's South Bank: A Self-Guided Audio Tour",
                        "null",
                        "<div><p>Walk from London Bridge to the Tate Modern while you explore the South Bank of the River Thames at your own pace on this affordable self-guided tour.\n\n• Follow in the footsteps of Chaucer and Shakespeare, and find out why being outside the City of London gave this bank of the river a murky reputation\n• Hear from producer Brian Cookson, a Blue Badge Guide and the author of two books of London walks\n• Do it all in 45 minutes or linger at stops along the way with complete control over when you start and finish\n• Get unlimited use before your booking date and after it\n• Use the virtual tour option at home\n\nOnce you’ve booked, you’ll receive a ticket with instructions and a unique code listed under “Before You Go”. Then simply install the VoiceMap app and enter your code. \n\nThe app displays directions to the starting point, and when you’re in the right place, just pop in your headphones and tap start. VoiceMap has automatic GPS playback, with turn-by-turn directions. It also works offline.</p></div>",
                        new ActivityResponse.GeoCode("51.5099787", "-0.0859812"),
                        List.of(
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzJmNWY3ZDI4LTY0MzItNGVlNS05Y2JiLTEwMDliMWMwMzZhYiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzBmYjIwN2IxLTFiOWQtNDMxZS04MjdiLTk0MzI3NDkwZmFmYiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzhjZTA2NzNhLTk0MTgtNDNhYi04MTVkLWM3YWRkZDc3MDg2MSIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzc5NWE0MGMzLTgyNWUtNGZkZC1hMjA5LTBhMTE5MDk5N2RiNyIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzL2U3OGQxYTgwLTUwNmUtNDMzMy05NTQwLTYxNDQzZDU1YWVmZCIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0="
                        ),
                        "https://amadeus.booking.holibob.tech/product/e3a959a2-4ea7-4f2c-a72f-369ede3b6376",
                        new ActivityResponse.Price("GBP", "6.0"),
                        "45 minutes",
                        "null"
                ),
                new ActivityResponse(
                        "137341038",
                        "activity",
                        "Build Your Own 7-hour Private London Tour in a Black Cab",
                        "null",
                        "<div><p>At <strong>Black Cab Heritage Tours</strong>, we have a wide range of private tours and day trips from London, but our priority is to make sure you have a memorable experience, and that every detail of your tour is taken care of.This “Build Your Own Tour” will allow you to work with one of our staff members to create the perfect customised tour according to your requests.These tours have been designed to be delivered within the city of London<strong>. If you want to do a day trip out of town or to a different area, please reach out directly so we can reconfirm the duration and the itinerary for the day.</strong>We look forward to welcoming you to London soon!\n\n<strong>Please note</strong>\nNo entrance fees are included\nGuides are not allowed to guide inside certain venues\nHotel Pick-Up &amp; Drop-Off  is only included from Central London\n</p></div>",
                        new ActivityResponse.GeoCode("51.5069873", "-0.123196"),
                        List.of(
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzL2IxNzRmMWM4LTg1ZDEtNDgwZS05NjNiLWYwYjVkNmNiNDA2MSIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzdlMGY4OWM5LWZkZTYtNDVlMS1hNzk3LWMyZmI5NDU2N2RhOCIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0="
                        ),
                        "https://amadeus.booking.holibob.tech/product/7cc5ca39-9bde-43d4-90c5-222469997f12",
                        new ActivityResponse.Price("GBP", "770.0"),
                        "7 hours",
                        "null"
                ),
                new ActivityResponse(
                        "139708856",
                        "activity",
                        "A Night at the Opera at Christmas by Candlelight",
                        "null",
                        "<div><p>Celebrate the magic of the festive season with an enchanting evening of passion, drama, and timeless music. Join the exceptional Piccadilly Sinfonietta and some of the UK’s finest operatic voices for a sparkling Christmas concert featuring a glittering selection of the greatest opera arias ever composed—performed in the atmospheric, candlelit setting of St Mary le Strand...</p></div>",
                        new ActivityResponse.GeoCode("51.51198", "-0.1166445"),
                        List.of(
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzU2Yjg3ZGVkLTIxNzctNGQyNy1hOTgyLTAwNmRjNGE0Y2I1ZCIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzL2I4MDZmMGRhLTY3OWYtNDY3OS1hMDNjLTAyZjViZDA2YmY3ZiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0="
                        ),
                        "https://amadeus.booking.holibob.tech/product/a2ba96ca-5959-4b29-9cec-766868ad335a",
                        new ActivityResponse.Price("GBP", "33.0"),
                        "1 hour",
                        "null"
                ),
                new ActivityResponse(
                        "139714559",
                        "activity",
                        "Rachmaninov Piano Concerto by Candlelight",
                        "null",
                        "<div><p>Step into the enchanting, candlelit sanctuary of St Mary le Strand, a stunning Baroque masterpiece nestled in the heart of London. Join The Piccadilly Sinfonietta and acclaimed pianist Warren Mailley-Smith for a captivating performance featuring Rachmaninov's deeply expressive and virtuosic 2nd Piano Concerto...</p></div>",
                        new ActivityResponse.GeoCode("51.51198", "-0.1166445"),
                        List.of(
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzgyYjhhZjUyLWQ4YjUtNDcwYi04ODA2LTJlMTIwZDVlOTEzOCIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0="
                        ),
                        "https://amadeus.booking.holibob.tech/product/9b5dbeba-ecfa-4945-b1d0-78e7d23369f6",
                        new ActivityResponse.Price("GBP", "33.0"),
                        "1 hour",
                        "null"
                ),
                new ActivityResponse(
                        "65717935",
                        "activity",
                        "Hyde Park and Kensington Gardens: A Self-Guided Audio Tour",
                        "null",
                        "<div><p>Explore two of London's most beautiful and famous Royal Parks with me. As a Londoner, I’ve grown up visiting Hyde Park and Kensington Gardens and I know all they have to offer, from their best spots to little-known vistas...</p></div>",
                        new ActivityResponse.GeoCode("51.51193", "-0.158467"),
                        List.of(
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzNmMDQ0NWY2LTMwZTEtNDJjMy04NGEzLWVkOWUyYjUyODc5YSIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzQxZTdjMjNlLWQ0M2UtNGM4Mi05ZmM4LWZhMWIzYTFhMWQ2NiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzL2RhZTQ3YzM5LWY1ZmMtNGM5Ni05YmM1LWNiNjcyNmUxY2RkMiIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzE2ZTQ3MDI0LTQwYWMtNDY2Yi05NGVhLTE5YTVhNTM3NGU1ZCIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzgxNTNjZmJiLTY0YzQtNGY2NS05NjMzLWM5MjcxNjVlYmYyMyIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0=",
                                "https://images.holibob.tech/eyJrZXkiOiJwcm9kdWN0SW1hZ2VzLzJlNzJhOWYzLTdhNTYtNGE0ZC1iNmUxLTdlYjdhNDI3NDM5OCIsImVkaXRzIjp7InJlc2l6ZSI6eyJmaXQiOiJjb3ZlciIsIndpZHRoIjoxOTIwLCJoZWlnaHQiOjEwODB9fX0="
                        ),
                        "https://amadeus.booking.holibob.tech/product/49f0d481-ce40-4ed7-9db9-81d3e623057a",
                        new ActivityResponse.Price("GBP", "9.0"),
                        "1 hour 8 minutes",
                        "null"
                )

        ));

        // Paris (48.8566, 2.3522)
        attractionsMap.put("48.8566,2.3522", Arrays.asList(
                new ActivityResponse(
                        "139365061",
                        "activity",
                        "Meet our Artist in Paris",
                        "<p>Do you dream of letting your imagination run wild in one of the most artistic cities in the world? WeLocArt invites you to live an immersive artistic experience in the heart of Paris.</p>",
                        "<p>Whether you are passionate about drawing, a lover of painting or curious to explore sculpture, WeLocArt offers tailor-made workshops, adapted to all levels.</p>\n<p>Why Choose WeLocArt?</p>\n<p>- Activity with recognized artists</p>\n<p>- Small Committee workshop for personalized attention.</p>\n<p>- Relaxed and stimulating atmosphere in an authentic Parisian artistic workshop.</p>\n<p>- Creativity and inspiration guaranteed, whether you are a beginner or an experienced.</p>",
                        new ActivityResponse.GeoCode("48.8575", "2.35138"),
                        List.of(
                                "https://cdn.regiondo.net/media/catalog/product/b/i/big-ticket-image-62d17cf36cf58245987431-cropped600-400-dpl-6321eebe40124.jpg"
                        ),
                        "https://amadeus-sherpa-es.regiondo.com/drawing-painting-or-sculpture-workshop-with-franck?vendor=38759",
                        new ActivityResponse.Price("EUR", "29.0"),
                        "1 hour",
                        "null"
                ),
                new ActivityResponse(
                        "139365053",
                        "activity",
                        "Half-day in Paris",
                        "<p>Discover our <strong>half-day artistic itinerary in Paris</strong>. It allows you to meet two artists that we select for you, according to your desires! Alone or with others, enhance this experience by treating your taste buds or enjoy a moment of well-being in a hotel-spa.</p>",
                        "<p>We offer you a personalised <strong>half-day artistic itinerary in Paris</strong> that allows you to meet two artists from our community. The choice is varied. We have many painters, sculptors and photographers.</p>\n<p> </p>\n<p>The meeting can take place either in the morning or in the afternoon for a duration of 4 hours.</p>\n<p> </p>\n<p>Here is an example of artists you might meet in Paris:</p>\n<ul>\n<li>Meeting with the painter Claire: an artist who draws and paints since her childhood. Her painting is defined by movement, light, material and colour. She works a lot around the body and its movement. She tries to translate emotions and deep feelings.</li>\n<li>Meeting with the sculptor Christine: an artist who works with clay, mainly black sandstone, a raw material, smoothed or polished but natural.</li>\n</ul>\n<div> </div>\n<p>You can find pictures of their works in the carousel to give you an idea of their work.</p>\n<p>This proposal is an indication, we organize your artistic itinerary according to your desires of the moment.</p>\n<p>Do not hesitate to contact us for more information: contact@welocart.fr - 01 84 25 99 88.</p>",
                        new ActivityResponse.GeoCode("48.8566", "2.35222"),
                        List.of(
                                "https://cdn.regiondo.net/media/catalog/product/b/i/big-ticket-image-62e8dbe536966974400926-cropped600-400.png"
                        ),
                        "https://amadeus-sherpa-es.regiondo.com/demi-journee-a-paris?vendor=38759",
                        new ActivityResponse.Price("EUR", "249.0"),
                        "4 hours",
                        "null"
                ),
                new ActivityResponse(
                        "139365006",
                        "activity",
                        "Food Tour in Paris - Farmers Market Food tour",
                        "<p>Lose yourself in French cuisine for 2h30 during our guided market tour in Paris. Discover the history of one of the most famous Parisian food markets and its district, meet the merchants and taste the best seasonal produce with a food expert guide!</p>",
                        "<p><strong>French Food Market Tour in Paris:</strong><br />Food markets are the heart of French and Parisien life. Friendly places for tasting and sharing, they are a key element of French cuisine.</p>\n<p>Our guides invite you to share this gourmet experience and discover some of the most famous Parisian food markets known for the quality of their products such as Aligre Market, Montorgueil and Levis streets.</p>\n<p>During this 2h30 tour, your guides will tell you about the history of the area you are visiting, the market and French culinary specialties. You will taste fresh produce and learn how to choose fruits and vegetables according to their use and season. All the while, our charming Parisian guides will not hesitate to share their favorite recipes.</p>\n<p>Soak up the atmosphere of Parisian life in a gourmet food market and enjoy the warm atmosphere of these exceptional places that tell the story of French cuisine.</p>\n<p>Moreover, our charming Parisian guides will not hesitate to share their favorite recipes throughout your guided tour in a gourmet food market in Paris!<br /><br /><br /><strong>Highlights of our Paris food Tour:</strong></p>\n<ul>\n<li>All our guides are French, bilingual and culinary experts.</li>\n<li>Personalization: we will select a market that meets your criteria.</li>\n<li>Your guide will customize tastings and adapt his speech to the audience (school or business groups, tourists or professionals).</li>\n</ul>\n<p><br /><strong>Practical information:</strong></p>\n<ul>\n<li><strong>Schedule</strong>: private tour every day from tuesday to sunday, in the morning only, at 10h30</li>\n<li><strong>Duration</strong>: 2h30</li>\n<li><strong>Languages available</strong>: French, English, Spanish (other languages upon request)</li>\n<li><strong>Group Rates (&gt;15 pers) and private tours</strong> : upon request infos@laroutedesgourmets.fr.</li>\n<li><strong>Purchase as a gift</strong>: offer this tour as a gift by choosing the option \" PURCHASE WITHOUT DATE\". We will send you a gift certificate via email. The gift is valid for a period of 6 months from the date of purchase and is not refundable.</li>\n<li><strong>Cancellation</strong>: Orders are considered confirmed upon reception of payment. For orders with date: all cancellations made more than 3 weeks prior to the reservation date will be fully refund, half of the payment will be reimbursed for cancellations within 2 weeks of the reservation date and no refund will be possible for cancellations less than one week before the date of the reservation</li>\n</ul>",
                        new ActivityResponse.GeoCode("48.8506", "2.37673"),
                        List.of(
                                "https://cdn.regiondo.net/media/catalog/product/b/i/big-ticket-image-5de6344eb39f0904168007-cropped600-400.jpeg"
                        ),
                        "https://amadeus-sherpa-es.regiondo.com/food-tour-in-paris-farmers-market-food-tour?vendor=38759",
                        new ActivityResponse.Price("EUR", "134.0"),
                        "2 hours",
                        "null"
                ),
                new ActivityResponse(
                        "139364991",
                        "activity",
                        "Premium Tour, Transport aéroport hôtel+ restauration +visite de Paris de 2 heures .",
                        "<p><span style=\"vertical-align:inherit;\">Visite d'une journée de Paris : transport aéroport Orly ou Paris Charles de Gaulle. + restauration le midi</span></p>",
                        "<p><span style=\"vertical-align:inherit;\">Ceci est un circuit touristique premium.</span></p>",
                        new ActivityResponse.GeoCode("48.8566", "2.35222"),
                        List.of(
                                "https://cdn.regiondo.net/media/catalog/product/b/i/big-ticket-image-60c32c4466ff5460559610-cropped600-400-dpl-60c53a06c1338.jpg"
                        ),
                        "https://amadeus-sherpa-es.regiondo.com/circuit-premium?vendor=38759",
                        new ActivityResponse.Price("EUR", "219.0"),
                        "4 hours",
                        "null"
                ),
                new ActivityResponse(
                        "139365052",
                        "activity",
                        "Artistic itineraries in Paris",
                        "<p>Discover our <strong>half-day artistic itinerary in Paris</strong>. It allows you to meet two artists that we select for you, according to your desires! Alone or with others, enhance this experience by treating your taste buds or enjoy a moment of well-being in a hotel-spa.</p>",
                        "<p>We offer personalized half-day, full-day or weekend art tours in Paris! These itineraries allow you to meet several artists of our community, to visit their studios and even to practice with them...</p>",
                        new ActivityResponse.GeoCode("48.8566", "2.35222"),
                        List.of(
                                "https://cdn.regiondo.net/media/catalog/product/b/i/big-ticket-image-62e8dbe536966974400926-cropped600-400-dpl-636285d31d80c.png"
                        ),
                        "https://amadeus-sherpa-es.regiondo.com/half-day-in-paris?vendor=38759",
                        new ActivityResponse.Price("EUR", "199.0"),
                        "",
                        "null"
                )
        ));

        return attractionsMap;
    }
}
