<?xml version='1.0'?>
<xsl:stylesheet
	version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="WT_WINDOW_CLOSE_CONFIG_DATA_TAG_NAME" priority="0" /> <!-- Hidden -->
    <xsl:template match="WT_CONFIG_DATA" priority="0" /> <!-- Hidden -->
    
     
	<xsl:template match="WT_SUMMARY">
		<html>
			<head>
				<title> Work timer </title>
			</head>

			<body bgcolor="#e0e0f0">
				<xsl:apply-templates/>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="WT_LISTING">
		<p/>
		<div align="center">
			<h2> Summary of time registration </h2>
		</div>
		<table cols="4" width="90%" align="center">
			<tr>
				<td width="25%"> </td>
				<td width="25%"> </td>
				<td width="25%"> </td>
				<td width="25%"> </td>
			</tr>
			<xsl:apply-templates/>
		</table>
	</xsl:template>
  
  
  
	<xsl:template match="WT_WEEK_SUMMARY">
		<p/>
		<div align="left">
			<h2> Week </h2>
		</div>
		<table rows="1" cols="3" width="90%" align="center">
			<tr>
        <th width="16%" bgcolor="#dddda0" align="center"> Global delta </th>
				<th width="12%" bgcolor="#ffffc0" align="center"> Weekly delta </th>     
        <th width="72%"  align="center"> </th>  
			</tr>
      <xsl:for-each select="WT_WEEK_SUMMARY_RECORD">
			<tr align="center" color="white">
        <td width="16%" bgcolor="#003399" > <font color="white"> <xsl:value-of select="WT_WEEK_SUMMARY_GLOBAL_DELTA" /> </font></td>
        <td width="12%" bgcolor="#663366" > <font color="white"> <xsl:value-of select="WT_WEEK_SUMMARY_WEEKLY_DELTA" /> </font></td>  
			</tr> 
      </xsl:for-each>      
		</table>
	</xsl:template>  
  
	<xsl:template match="WT_MONTH_SUMMARY">
		<p/>
		<div align="left">
			<h2> Month </h2>
		</div>
		<table rows="1" cols="2" width="90%" align="center">
			<tr>
        <th width="16%" bgcolor="#dddda0" align="center">Month summary delta</th>
        <th width="84%"  align="center"> </th>  
			</tr>
      <xsl:for-each select="WT_MONTH_SUMMARY_RECORD">
			<tr align="center" color="white">
        <td width="16%" bgcolor="#003399" > <font color="white"> <xsl:value-of select="WT_MONTH_SUMMARY_DELTA_HOURS" /> </font></td>
			</tr> 
      </xsl:for-each>      
		</table>
	</xsl:template>
  
  
  

	<xsl:template match="WT_DAY_SUMMARY">
		<p/>
		<table width="90%" rows="8" align="center">
    <xsl:for-each select="WT_HEADER">
			<tr>
        <th width="16%" bgcolor="#dddda0" align="center"> Date </th>
				<th width="12%" bgcolor="#ffffc0" align="center"> Daily delta </th>
				<th width="12%" bgcolor="#ffffc0" align="center"> Global delta </th>
				<th width="12%" bgcolor="#9999CC" align="center"> <xsl:value-of select="H_TIMER_1" /> </th>
				<th width="12%" bgcolor="#AAAAEE" align="center"> <xsl:value-of select="H_TIMER_2" /> </th>
				<th width="12%" bgcolor="#9999CC" align="center"> <xsl:value-of select="H_TIMER_3" /> </th>
				<th width="12%" bgcolor="#AAAAEE" align="center"> <xsl:value-of select="H_TIMER_4" /> </th>
        <th width="12%" bgcolor="#9999CC" align="center"> <xsl:value-of select="H_TIMER_5" /> </th>
			</tr>
     </xsl:for-each>
			<xsl:for-each select="WT_RECORD">
				<tr align="center" bgcolor="lightgreen">
          <td> <xsl:value-of select="DATE" /> </td>
					<td> <xsl:value-of select="DAILY_DELTA" /> </td>
					<td> <xsl:value-of select="GLOBAL_DELTA" /> </td>
					<td bgcolor="#99FFCC"> <xsl:value-of select="TIMER_1" /> </td>
					<td bgcolor="#99FFFF"> <xsl:value-of select="TIMER_2" /> </td>
					<td bgcolor="#99FFCC"> <xsl:value-of select="TIMER_3" /> </td>
					<td bgcolor="#99FFFF"> <xsl:value-of select="TIMER_4" /> </td>
          <td bgcolor="#99FFCC"> <xsl:value-of select="TIMER_5" /> </td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>

</xsl:stylesheet>
