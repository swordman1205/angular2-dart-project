part of app_facade;


class Assay {
  
  
  String id = null;
  

  
  String name = null;
  

  
  String unit = null;
  //enum unitEnum {  PERCENT,  MCG_DL,  MG_DL,  MG_L,  NG_ML,  U_L,  UG_DL,  ULU_ML,  };

  
  double rangeMin = null;
  

  
  double rangeMax = null;
  
  Assay();

  @override
  String toString()  {
    return 'Assay[id=$id, name=$name, unit=$unit, rangeMin=$rangeMin, rangeMax=$rangeMax, ]';
  }
}

