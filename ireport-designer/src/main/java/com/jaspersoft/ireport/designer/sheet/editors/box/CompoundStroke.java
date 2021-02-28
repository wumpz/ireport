/*
 * Copyright 2006 Jerry Huxtable.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jaspersoft.ireport.designer.sheet.editors.box;

import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;

/**
 * @author Jerry Huxtable
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class CompoundStroke implements Stroke {
   public final static int ADD = 0;
   public final static int DIFFERENCE = 3;
   public final static int INTERSECT = 2;
   public final static int SUBTRACT = 1;

   private int operation = ADD;
   private Stroke stroke1, stroke2;

   public CompoundStroke() {
   }

   public CompoundStroke( Stroke stroke1, Stroke stroke2, int operation ) {
      this.stroke1 = stroke1;
      this.stroke2 = stroke2;
      this.operation = operation;
   }

   public Shape createStrokedShape( Shape shape ) {
      if( stroke1 == null ){
         throw new IllegalArgumentException( "stroke1 is null" );
      }
      if( stroke2 == null ){
         throw new IllegalArgumentException( "stroke2 is null" );
      }

      Area area1 = new Area( stroke1.createStrokedShape( shape ) );
      Area area2 = new Area( stroke2.createStrokedShape( shape ) );
      switch( operation ){
         case ADD:
            area1.add( area2 );
            break;
         case SUBTRACT:
            area1.subtract( area2 );
            break;
         case INTERSECT:
            area1.intersect( area2 );
            break;
         case DIFFERENCE:
            area1.exclusiveOr( area2 );
            break;
      }
      return area1;
   }

   public int getOperation() {
      return operation;
   }

   public Stroke getStroke1() {
      return stroke1;
   }

   public Stroke getStroke2() {
      return stroke2;
   }

   public void setOperation( int operation ) {
      this.operation = operation;
   }

   public void setStroke1( Stroke stroke1 ) {
      this.stroke1 = stroke1;
   }

   public void setStroke2( Stroke stroke2 ) {
      this.stroke2 = stroke2;
   }
}