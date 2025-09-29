/*
 *  Copyright 2020. S.O.X. All rights reserved
 */

package com.ithows.util.string;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class StringSorter
 * 
 * @author Roi Kim <S.O.X Co. Ltd.>
 */
public class StringSorter {

    
    public static void main(String[] args) {
        String[] key = {"cc", "aa", "bb", "aa", "dd"};
        int[] val = {10, 20, 30, 40, 50};
        LinkedHashMap<String, Object> output = null;

        output = sortedArrayNonDuplicate(key, convertObjectArray(val));
        for(String keyStr : output.keySet()){
            System.out.println(keyStr + " = " + Integer.parseInt(output.get(keyStr).toString()) ) ;
        }
        
        String[] vals = {"100", "22", "33", "44", "55"};
        int[] array = Arrays.stream(vals)
                        .mapToInt(Integer::parseInt)
                        .toArray();
        
        for(int v : array){
            System.out.println(""+ v) ;
        }

        int[] intArray = { 1, 2, 3, 4, 5 };
 
        String[] strArray = Arrays.stream(intArray)
                                .mapToObj(String::valueOf)
                                .toArray(String[]::new);
        
        for(String k : strArray){
            System.out.println(""+ k) ;
        }
    }
    
    private static Object[] convertObjectArray(Object val){
        if (val instanceof Object[])
           return (Object[])val;
        int arrlength = Array.getLength(val);
        Object[] outputArray = new Object[arrlength];
        for(int i = 0; i < arrlength; ++i){
           outputArray[i] = Array.get(val, i);
        }
        return outputArray;
    }
    
    // 단일 소팅
    public static void sort(String[] a) {
            if (!alreadySorted(a))
                    sort(a, 0, a.length - 1);
    }

    // 연관 배열에 대한 소팅
    public static void sort(String[] main, String[] sub) {
            if (!alreadySorted(main))
                    sort(main, sub, 0, main.length - 1);
    }

    public static void sort(String[] main, int[] sub) {
            if (!alreadySorted(main))
                    sort(main, sub, 0, main.length - 1);
    }

    public static void sort(String[] main, double[] sub) {
            if (!alreadySorted(main))
                    sort(main, sub, 0, main.length - 1);
    }

    static void sort(String[] a, int from, int to) {
            int i = from, j = to;
            String center = a[ (from + to) / 2 ];
            do {
                    while ( i < to && center.compareTo(a[i]) > 0 ) i++;
                    while ( j > from && center.compareTo(a[j]) < 0 ) j--;
                    if (i < j) {
                        String temp = a[i]; 
                        a[i] = a[j]; 
                        a[j] = temp; 
                    }
                    if (i <= j) { i++; j--; }
            } while(i <= j);
            if (from < j) sort(a, from, j);
            if (i < to) sort(a,  i, to);
    }

    static boolean alreadySorted(String[] a) {
            for ( int i=1; i<a.length; i++ ) {
                    if (a[i].compareTo(a[i-1]) < 0 )
                    return false;
            }
            return true;
    }

    static void sort(String[] main, String[] sub, int from, int to) {
            int i = from, j = to;

            if(main.length > sub.length){
                return;
            }

            String center = main[ (from + to) / 2 ];
            do {
                    while ( i < to && center.compareTo(main[i]) > 0 ) i++;
                    while ( j > from && center.compareTo(main[j]) < 0 ) j--;
                    if (i < j) {
                        String temp = main[i]; 
                        main[i] = main[j]; 
                        main[j] = temp; 

                        String temp2 = sub[i]; 
                        sub[i] = sub[j]; 
                        sub[j] = temp2; 

                    }
                    if (i <= j) { i++; j--; }
            } while(i <= j);
            if (from < j) sort(main, from, j);
            if (i < to) sort(main,  i, to);
    }


    static void sort(String[] main, int[] sub, int from, int to) {
            int i = from, j = to;

            if(main.length > sub.length){
                return;
            }

            String center = main[ (from + to) / 2 ];
            do {
                    while ( i < to && center.compareTo(main[i]) > 0 ) i++;
                    while ( j > from && center.compareTo(main[j]) < 0 ) j--;
                    if (i < j) {
                        String temp = main[i]; 
                        main[i] = main[j]; 
                        main[j] = temp; 

                        int temp2 = sub[i]; 
                        sub[i] = sub[j]; 
                        sub[j] = temp2; 

                    }
                    if (i <= j) { i++; j--; }
            } while(i <= j);
            if (from < j) sort(main, from, j);
            if (i < to) sort(main,  i, to);
    }


    static void sort(String[] main, double[] sub, int from, int to) {
            int i = from, j = to;

            if(main.length > sub.length){
                return;
            }

            String center = main[ (from + to) / 2 ];
            do {
                    while ( i < to && center.compareTo(main[i]) > 0 ) i++;
                    while ( j > from && center.compareTo(main[j]) < 0 ) j--;
                    if (i < j) {
                        String temp = main[i]; 
                        main[i] = main[j]; 
                        main[j] = temp; 

                        double temp2 = sub[i]; 
                        sub[i] = sub[j]; 
                        sub[j] = temp2; 

                    }
                    if (i <= j) { i++; j--; }
            } while(i <= j);
            if (from < j) sort(main, from, j);
            if (i < to) sort(main,  i, to);
    }
	


    // 중복제거 소팅 (키는 첫번째 인자 값은 두번째 인자로 채움)
    public static LinkedHashMap<String, Object> sortedArrayNonDuplicate(String[] elements1, Object[] elements2){
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        // 1. 중복 처리
        for(int i=0 ; i<elements1.length ; i++){
            if(elements1[i] != null && !elements1[i].equals("") && elements2[i] != null && !elements2[i].equals("")){

                // 이미 같은 게 있는 지
                if( map.get(elements1[i]) != null && !map.get(elements1[i]).equals("")){

                       // @@ 값 변경 조건 지정시
//                    try {
//                        double preValue = Double.parseDouble(map.get(elements1[i]));
//                        double nowValue = Double.parseDouble(elements2[i]);
//
//                        if(preValue <= nowValue){
//                            map.put(elements1[i], elements2[i]);
//                        }
//
//                    } catch (Exception e) {
//                        System.out.println("parseSortedMapCSV : Value Parse Error");
//                    }

                }else{  // 같은 게 없으면 넣음
                    map.put(elements1[i], elements2[i]);
                }

            }
        }
        map = sortMapByKey(map, true);
        
        return map;
    }


    // Map 소팅 (중복을 제거해 줌)
    private static LinkedHashMap<String, Object> sortMapByKey(Map<String, Object> map, boolean isAsc) {
        List<Map.Entry<String, Object>> entries = new LinkedList<>(map.entrySet());

        if(isAsc){
            Collections.sort(entries, (o1, o2) -> o1.getKey().compareTo(o2.getKey()));
        }else{
            Collections.sort(entries, (o1, o2) -> o2.getKey().compareTo(o1.getKey()));
        }

        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : entries) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
        


    // 숫자 순으로 
    public static String[] sortNumerically(String[] list) {
            int n = list.length;
            String[] paddedList = getPaddedNames(list);
            String[] sortedList = new String[n];
            int[] indexes = Tools.rank(paddedList);
            for (int i = 0; i < n; i++)
                    sortedList[i] = list[indexes[i]];
            return sortedList;
    }

    // Pads individual numeric string components with zeroes for correct sorting
    private static String[] getPaddedNames(String[] names) {
            int nNames = names.length;
            String[] paddedNames = new String[nNames];
            int maxLen = 0;
            for (int jj = 0; jj < nNames; jj++) {
                    if (names[jj].length() > maxLen) {
                            maxLen = names[jj].length();
                    }
            }
            int maxNums = maxLen / 2 + 1;//calc array sizes
            int[][] numberStarts = new int[names.length][maxNums];
            int[][] numberLengths = new int[names.length][maxNums];
            int[] maxDigits = new int[maxNums];

            //a) record position and digit count of 1st, 2nd, .. n-th number in string
            for (int jj = 0; jj < names.length; jj++) {
                    String name = names[jj];
                    boolean inNumber = false;
                    int nNumbers = 0;
                    int nDigits = 0;
                    for (int pos = 0; pos < name.length(); pos++) {
                            boolean isDigit = name.charAt(pos) >= '0' && name.charAt(pos) <= '9';
                            if (isDigit) {
                                    nDigits++;
                                    if (!inNumber) {
                                            numberStarts[jj][nNumbers] = pos;
                                            inNumber = true;
                                    }
                            }
                            if (inNumber && (!isDigit || (pos == name.length() - 1))) {
                                    inNumber = false;
                                    if (maxDigits[nNumbers] < nDigits) {
                                            maxDigits[nNumbers] = nDigits;
                                    }
                                    numberLengths[jj][nNumbers] = nDigits;
                                    nNumbers++;
                                    nDigits = 0;
                            }
                    }
            }

            //b) perform padding
            for (int jj = 0; jj < names.length; jj++) {
                    String name = names[jj];
                    int numIndex = 0;
                    StringBuilder destName = new StringBuilder();
                    for (int srcPtr = 0; srcPtr < name.length(); srcPtr++) {
                            if (srcPtr == numberStarts[jj][numIndex]) {
                                    int numLen = numberLengths[jj][numIndex];
                                    if (numLen > 0) {
                                            for (int pad = 0; pad < (maxDigits[numIndex] - numLen); pad++) {
                                                    destName.append('0');
                                            }
                                    }
                                    numIndex++;
                            }
                            destName.append(name.charAt(srcPtr));
                    }
                    paddedNames[jj] = destName.toString();
            }
            return paddedNames;
    }
}
