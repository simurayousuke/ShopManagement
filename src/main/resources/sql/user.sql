#sql("findByUsername")
SELECT * FROM _users WHERE username = #para(0)
#end
#sql("findByIdentifier")
SELECT * FROM _users WHERE identifier = #para(0)
#end
