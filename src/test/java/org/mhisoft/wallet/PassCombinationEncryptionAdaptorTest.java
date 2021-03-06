/*
 *
 *  * Copyright (c) 2014- MHISoft LLC and/or its affiliates. All rights reserved.
 *  * Licensed to MHISoft LLC under one or more contributor
 *  * license agreements. See the NOTICE file distributed with
 *  * this work for additional information regarding copyright
 *  * ownership. MHISoft LLC licenses this file to you under
 *  * the Apache License, Version 2.0 (the "License"); you may
 *  * not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied.  See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 *
 */

package org.mhisoft.wallet;

import org.junit.Test;
import org.mhisoft.wallet.model.PassCombinationEncryptionAdaptor;
import org.testng.Assert;

/**
 * Description:
 *
 * @author Tony Xue
 * @since Dec, 2017
 */
public class PassCombinationEncryptionAdaptorTest {

	@Test
	public void test1() {
		PassCombinationEncryptionAdaptor pass = new PassCombinationEncryptionAdaptor("Test123!","123");
		Assert.assertEquals(pass.getCombination(),"123");
		Assert.assertEquals(pass.getPass(),"Test123!");
		Assert.assertEquals(pass.getPassAndCombination(),"Test123!123");

		pass.setCombination("2","3","4");
		Assert.assertEquals(pass.getSpinner1(),"2");
		Assert.assertEquals(pass.getSpinner2(),"3");
		Assert.assertEquals(pass.getSpinner3(),"4");

		pass.setPass("newPa&!@@");
		Assert.assertEquals(pass.getPass(),"newPa&!@@");


	}


}
