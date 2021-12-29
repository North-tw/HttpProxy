package com.nv.commons.type;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum WebsiteType {

	Home(0),
	Bavet(1),
	MPSB(2),
	NETCAFE(3),
	Site928club(4),
	SiteAmbbet(5),
	SiteSlotCiti88(6),
	SiteAvexis(7),
	SiteMiiWin(8),
	SiteVegas79(9),
	SiteInCash(10),
	SiteAe168(11),
	SiteLoveBall365(12),
	SiteBetSun(13),
	SiteSb234(14),
	SiteGama(15),
	SiteTbkbet(16),
	SiteKhmer(17),
	Site777ww(18),
	SiteSzweinet(19),
	VenusMPSB(20),
	SiteGoal168(21),
	SiteProbet77(22),
	VenusAe888(23),
	SiteAgentJet(24),
	VenusAgentJet(25),
	SiteGamaWL(26),
	SiteCSRoyal88(27),
	SiteC9Clubs(28),
	SiteSureWin365(29),
	SiteKaisargaming(30),
	SiteSin368(31),
	SiteDD88Bet(32),
	venusNETCAFE(33),
	SiteLebo(34),
	PHCAFE(35),
	venusPHCAFE(36),
	SiteGPMX(37),
	SiteKaisarVenus(38),
	MexAe888(39),
	venusGPMX(40),
	SiteECLBet(41),
	siteZchwan(42),
	MexSzweinet(43),
	SiteInplay(44),
	SiteLeying(45),
	MexDreamPlay(46),
	MexShengBet(47),
	MexAibo(48),
	MexTogel(49),
	MexAWC(50),
	VenusAWC(51),
	MexRWB(52),
	MexUBO(53),
	MexGamingsoft(54),
	MexLescome(55),
	Mexqq288(56),
	MexZch002(57),
	MexGarudabet(58),
	MexVnc88(59),
	MexV1scr(60),
	VenusV1scr(61),
	MexTgvip(62),
	VenusTgvip(63),
	MexHackbet(64),
	Mex998v2(65),
	MexArena777(66),
	MexAskmebet(67),
	Mex928club(68),
	MexMaxim99(69),
	Mex12play(70),
	MexMarduo(71),
	MexTigerii(72),
	MexS36bet(73),
	MexWc88(74),
	MexMysnet88(75),
	Mex1xbet(76),
	MexAces33(77),
	Venus928club(78),
	MexMbthai88(79),
	MexEnjoy11(80),
	MexOO88(81),
	MexIG(82),
	Mex757group(83),
	MexHao666vip(84),
	MexPangu(85),
	MexCa17888(86),
	MexLy929(87),
	MexZm5666(88),
	venusSenComm(89),
	MexBongvn88(90),
	MexCasino168(91),
	MexTMTgaming(92),
	Mex918jubb(93),
	MexOg(94),
	MexBCbaccarat(95),
	MexGameintact(96),
	VenusBcg(97),
	MexGreenx88(98),
	MexIronbet(99),
	MexMgm99win(100),
	VenusBcbaccarat(101),
	MexAgp88(102),
	VenusAgp88(103),
	MexZch008(104),
	MexAplus(105),
	MexCasinoxb(106),
	VenusCasinoxb(107),
	Mexmoon88(108),
	MexGreenstar(109),
	MexSingbo55(110),
	MexZch013(111),
	MexSvo888(112),
	MexRuby9(113),
	MexGplay99(114),
	MexYb(115),
	MexSexy168(116),
	MexP2pworld(117),
	MexGoldbet888(118),
	MexNaga88king(119),
	MexP1(120),
	MexKingslot9(121),
	MexKinglotto9(122),
	MexP2(123),
	MexLeshang(124),
	MexJiubo(125),
	MexCmm5w(126),
	MexTh5ace(127),
	MexViet8(128),
	MexFoxz168(129),
	MexMx3(130),
	MexMxOle(131),
	MexGpk(132),
	MexSbo(133),
	MexWingame(134),
	MexGamestone(135),
	MexOriental(136),
	VenusGamestone(137),
	MexSboUat(138),
	Mexiw88(139),
	MexGsm(140),
	MexHbthb(141),
	MexWynn35(142),
	MexTcg(143),
	MexTfa(144),
	MexXinbo(145),
	MexDfbsg(146),
	MexGygroup(147),
	MexSpbet(148),
	MexWWbet(149),
	Mexqkun(150),
	MexBetdeal(151),
	MexBallbet(152),
	MexYfkj(153),
	MexMbsg88(154),
	MexAsia9club(155),
	MexNg(156),
	MexMbet(157),
	MexKz(158),
	Mex18ace(159),
	MexPlay555(160),
	Mex123bet(161),
	MexOg2(162),
	MexRome(163),
	MexHYBBN(164),
	MexXingcai(165),
	MexBestbet(166),
	MexJiexun(167),
	MexTbsclubs(168),
	MexRoti88(169),
	MexKosun(170),
	Mexibclub(171),
	MexTitancraft(172),
	MexPl5(173),
	MexSexygame66(174),
	MexScs188(175),
	MexMbet2(176),
	Mexi8Club(177),
	MexAsia999(178),
	Mexifm789plus(179),
	MexAechess(180),
	MexSolarbet(181),
	MexVivabet(182),
	MexZch014(183),
	MexBet1s(184),
	MexComebetna(185),
	MexGsbet365(186),
	MexXingcai888(187),
	MexAsiawin99(188),
	VenusGama(189),
	VenusGamaWL(190),
	MexWseven(191),
	MexWebet22(192),
	MexH3bet(193),
	MexManbetX(194),
	MexSexyth(195),
	MexAllstar55(196),
	MexBsbbet(197),
	MexPickplay24(198),
	Mex66Sexygame(199),
	MexScth(200),
	Mexharimaubet(201),
	MexZhousi(202),
	MexSexymm(203),
	MexKFRT(204),
	MexAGST(205),
	MexWinningw88(206),
	MexSlot33(207),
	MexDwin378(208),
	MexOn9bet(209),
	MexNew1b(210),
	MexWasabi(211),
	MexJincaishen(212),
	MextestXingyao(213),
	MexXingyao666(214),
	MexEgroup88(215),
	MexGg88club(216),
	MexBet123(217),
	MexLuckbet88(218),
	MexKkslot(219),
	MexVmwin(220),
	MexKingpin88(221),
	MexSbobet111(222),
	MexWinbet(223),
	MexBoombet(224),
	Mex5gbet(225),
	MexUfa247(226),
	MexZch018(227),
	MexGoal6969(228),
	MexTcgseamless(229),
	Mexlioncitybet(230),
	MexJcash(231),
	MexCmm4w(232),
	MexSignbet(233),
	MexAfb99(234),
	Mexmahagame66(235),
	Mexisb888(236),
	Mex82922(237),
	Mexeuwinsg(238),
	Mex1betwin(239),
	MexOn9win(240),
	MexIproBet(241),
	MexAsiaBignet(242),
	MexQ11toto(243),
	MexSe99(244),
	MexSuperXClub(245),
	MexFastbet98(246),
	MexCWA(247),
	VenusCWA(248),
	MexCakraBet(249),
	MexTopplay(250),
	MexSun789(251),
	MexAWC2(252),
	VenusAWC2(253),
	MexAWC3(254),
	VenusAWC3(255),
	MexAWC4(256),
	VenusAWC4(257),
	Mex7up(258),
	MexNova88(259),
	MexMoney88(260),
	MexXBB(261),
	MexAllbet(262),
	MexStreamerAIG(263),
	MexAWCA(264),
	VenusAWCA(265),
	MexAWCA2(266),
	VenusAWCA2(267),
	MexAWCA3(268),
	VenusAWCA3(269),
	MexAWCA4(270),
	VenusAWCA4(271),
	MexAWC5(272),
	VenusAWC5(273),
	MexAWCA5(274),
	VenusAWCA5(275),
	MexNova88net(276),
	MexSexyHacker(277),
	MexAELine(278),
	MexJumbo(279),
	MexCatLive(280),
	MexTest7up(281),
	MexDemoSite(282),
	ARS(283),
	VenusARS(284),
	LiveStreamer(285),
	MexMoney66(286),
	MexAWCA6(287),
	MexAWCA7(288),
	MexAWCA8(289),
	MexAWCA9(290),
	AESevenAWC(291),
	AESeven(292),
	MexAWCDemo(293),
	MexAWCADemo(294),
	LiveStreamerDemo(295),
	MexLiveBacca(296),
	AESevenAWCA(297),
	AESevenAWCA2(298),
	AESevenAWCA3(299),
	AESevenAWCA4(300),
	AESevenAWCA5(301),
	MexAWS031(302),
	MexAWS032(303),
	MexAWS033(304),
	MexAWS034(305),
	MexAWS035(306),
	MexAWS036(307),
	MexAWS037(308),
	MexAWS038(309),
	AESevenAWS031(310),
	AESevenAWS032(311),
	AESevenAWS033(312),
	AESevenAWS034(313),
	AESevenAWS035(314),
	AESevenAWS036(315),
	AESevenAWS037(316),
	AESevenAWS038(317),
	VenusAWS03(318),
	MexAWS061(319),
	VenusAWS06(320),
	TestDefaultApiAction(901),
	TestAWCApiAction(902),
	TestLargeAmountSplitApiAction(903),
	TestMPSBApiAction(904),
	TestNETCAFEApiAction(905),
	TestSite1xBetApiAction(906),
	TestSiteAmbbetApiAction(907),
	TestSiteSlotCiti88ApiAction(908),
	TestAESevenAWC(909),
	Mex1199119(995),
	DemoMexCn(996),
	venusMarcTest(997),
	MarcTest(998),
	UNITTest(999),
	;

	public final static List<WebsiteType> VALUES = Collections
		.unmodifiableList(Stream.of(WebsiteType.values()).collect(Collectors.toList()));

	public static Optional<WebsiteType> get(int unique) {
		return WebsiteType.VALUES.stream().filter(x -> x.unique == unique).findFirst();
	}

	public static Optional<WebsiteType> get(String name) {
		return WebsiteType.VALUES.stream().filter(e -> e.name().equalsIgnoreCase(name)).findFirst();
	}

	private final int unique;

	private WebsiteType(int unique) {
		this.unique = unique;
	}

	public int unique() {
		return unique;
	}
}